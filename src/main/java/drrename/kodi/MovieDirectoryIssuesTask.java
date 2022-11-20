package drrename.kodi;

import drrename.kodi.nfo.MovieDbLookupTreeItemValue;
import drrename.kodi.nfo.NfoFileCollector;
import drrename.kodi.nfo.NfoFileParser;
import drrename.model.RenamingPath;
import drrename.ui.kodi.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Getter
@Slf4j
class MovieDirectoryIssuesTask extends Task<Void> {
    private final Path directory;

    private final FilterableKodiRootTreeItem rootTreeItem;

    private final Executor executor;

    private final MovieDbClientFactory movieDbClientFactory;

    private final WarningsConfig warningsConfig;

    private final Observable[] extractor;

    private void verifyState() {
        Objects.requireNonNull(directory);
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.getFileName() + " is not a directory");
        }
        Objects.requireNonNull(rootTreeItem);
        Objects.requireNonNull(executor);
    }

    @Override
    protected Void call() throws Exception {
        verifyState();
        var nfoFileParser = new NfoFileParser();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(directory)) {

            for (Path path : ds) {
                if (isCancelled()) {
                    log.info("Cancelling");
                    break;
                }
                if (Files.isDirectory(path)) {
                    if (path.getFileName().toString().startsWith("@")) {
                        log.debug("Ignoring {}", path);
                        continue;
                    }

                    var renamingPath = new RenamingPath(path);
                    var itemValue = new MovieTreeItemValue(renamingPath, executor, warningsConfig);

                    var nfoFiles = new NfoFileCollector().collectNfoFiles(path);
                    if(!nfoFiles.isEmpty()){
                        Path firstNfoFile = nfoFiles.get(0);
                        String movieNameFromNfoFile = new NfoFileTitleExtractor(nfoFileParser).getTitleFromNfoFile(firstNfoFile);
                        if(StringUtils.isNotBlank(movieNameFromNfoFile)){
                            var movieDBClient = movieDbClientFactory.getNewMovieDbChecker().getClient();
                        }
                        if(StringUtils.isNotBlank(movieNameFromNfoFile)){
                            itemValue.setMovieNameFromNfo(movieNameFromNfoFile);
                        }
                    }


                    var item = new MovieTreeItemFilterable(itemValue, extractor);
                    List<FilterableKodiTreeItem> childItems = buildChildItems(renamingPath);
                    childItems.forEach(childItem -> Platform.runLater(() -> item.getSourceChildren().add(childItem)));
                    Platform.runLater(() -> getRootTreeItem().getSourceChildren().add(item));
                }
            }
        }
        return null;
    }

    List<FilterableKodiTreeItem> buildChildItems(RenamingPath renamingPath) {
        List<FilterableKodiTreeItem> treeItems = new ArrayList<>();
        List<KodiTreeItemValue<?>> treeItemValues = Arrays.asList(
                new MovieDbLookupTreeItemValue(renamingPath, executor, movieDbClientFactory, warningsConfig),
                new NfoFileNameTreeItemValue(renamingPath, executor, warningsConfig),
                new MediaFileNameTreeItemValue(renamingPath, executor, warningsConfig),
                new NfoFileContentMovieNameTreeItemValue(renamingPath, executor, warningsConfig)
        );

        for (KodiTreeItemValue<?> v : treeItemValues) {
            treeItems.add(new FilterableKodiTreeItem(v, null));
            executeItemValue(v);
        }

        return treeItems;
    }

    <R> void executeItemValue(KodiTreeItemValue<R> itemValue) {
        var fixableStatusChecker = new FixableStatusChecker<>(itemValue);
        fixableStatusChecker.setOnFailed(itemValue::defaultTaskFailed);
        fixableStatusChecker.setOnSucceeded(event -> statusCheckerSucceeded(itemValue, event));
//        log.debug("Running {}", itemValue);
        getExecutor().execute(fixableStatusChecker);
    }

    <R> void statusCheckerSucceeded(KodiTreeItemValue<R> itemValue, WorkerStateEvent event) {
        itemValue.updateStatus((R) event.getSource().getValue());
    }

}
