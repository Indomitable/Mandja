/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.db.BaseRepository;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.ArrayList;

public class IdLoader extends ISmallPreviewLoader {
	protected final Integer pageSize = 10;
	protected long currentId = 0;
	protected long maxId = 0;
	private BaseRepository repository;

	public Boolean hasMore() {
		if (this.maxId == 0)
			maxId = repository.getMaxId();
		return currentId < this.maxId;
	}

	@Override
	public ArrayList<PreviewListItem> getNextData() {
		long startId = currentId;
		ArrayList<PreviewListItem> data = repository.getPreviews(startId, sorting);
		currentId = startId + pageSize;
		return data;
	}

	public void setRepository(BaseRepository repository) {
		this.repository = repository;
	}

}
