/*
 *     Dr.Rename - A Minimalistic Batch Renamer
 *
 *     Copyright (C) 2022
 *
 *     This file is part of Dr.Rename.
 *
 *     You can redistribute it and/or modify it under the terms of the GNU Affero
 *     General Public License as published by the Free Software Foundation, either
 *     version 3 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful, but WITHOUT
 *     ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *     FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 *     for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package drrename.kodi.ui;

import drrename.kodi.FilterableTreeItem;
import drrename.util.DrRenameUtil;
import javafx.beans.Observable;
import javafx.scene.control.TreeItem;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;

@Slf4j
public class FilterableKodiTreeItem extends FilterableTreeItem<KodiTreeItemValue<?>> {

    private final Observable[] extractor;

    public FilterableKodiTreeItem(KodiTreeItemValue value, Observable[] extractor) {
        super(value);
        this.extractor = extractor;
        value.setTreeItem(this);
        graphicProperty().bind(value.graphicProperty());
    }

    @Override
    protected Comparator<TreeItem<KodiTreeItemValue<?>>> getComparator() {
        return Comparator.comparing(o -> o.getValue().getRenamingPath().getFileName());
    }

    protected Observable[] getExtractorCallback(TreeItem<KodiTreeItemValue<?>> item) {
        return DrRenameUtil.concatenate(new Observable[]{item.getValue().warningProperty()}, extractor);
    }

    public void add(FilterableKodiTreeItem childItem) {
        getSourceChildren().add(childItem);
    }
}