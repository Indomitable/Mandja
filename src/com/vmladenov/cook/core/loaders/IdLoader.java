package com.vmladenov.cook.core.loaders;

import java.util.ArrayList;

import com.vmladenov.cook.core.db.BaseRepository;
import com.vmladenov.cook.domain.PreviewListItem;

public class IdLoader extends ISmallPreviewLoader {
    protected final Integer pageSize = 10;
    protected long currentId = 0;
    protected long maxId = 0;
    private BaseRepository repository;

    public Boolean hasMore() {
        if (this.maxId == 0) maxId = getRepository().getMaxId();
        return currentId < this.maxId;
    }

    @Override
    public ArrayList<PreviewListItem> getNextData() {
        long startId = currentId;
        ArrayList<PreviewListItem> data = getRepository().getPreviews(startId, sorting);
        currentId = startId + pageSize;
        return data;
    }

    public void setRepository(BaseRepository repository) {
        this.repository = repository;
    }

    public BaseRepository getRepository() {
        return repository;
    }
}
