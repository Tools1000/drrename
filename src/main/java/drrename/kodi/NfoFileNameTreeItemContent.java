/*
 *     Dr.Rename - A Minimalistic Batch Renamer
 *
 *     Copyright (C) 2022
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package drrename.kodi;

import java.util.stream.Collectors;

public class NfoFileNameTreeItemContent extends CheckResultTreeItemContent<NfoFileNameCheckResult> {

    public NfoFileNameTreeItemContent(NfoFileNameCheckResult checkResult) {
        super(checkResult);
    }

    @Override
    protected boolean hasWarning() {
        return !NfoFileNameType.MOVIE_NAME.equals(getCheckResult().getType()) && !NfoFileNameType.DEFAULT_NAME.equals(getCheckResult().getType());
    }

    @Override
    public String toString() {
        if(hasWarning()){
            return super.toString() + " " + getCheckResult().getNfoFiles().stream().map(e -> e.getFileName().toString()).collect(Collectors.joining(", "));
        }
        return super.toString();
    }
}