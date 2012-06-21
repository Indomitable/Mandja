package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.db.ISmallPreviewListRepository;
import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public class IdLoader extends ISmallPreviewLoader {
    protected final Integer pageSize = 10;
    protected long currentId = 0;
    protected long maxId = 0;
    private ISmallPreviewListRepository repository;

    public Boolean hasMore() {
        if (this.maxId == 0) maxId = getRepository().getMaxId();
        return currentId < this.maxId;
    }

    @Override
    public ArrayList<SmallPreview> getNextData() {
        long startId = currentId;
        ArrayList<SmallPreview> data = getRepository().getPreviews(startId, sorting);
        currentId = startId + pageSize;
        return data;
    }

    public void setRepository(ISmallPreviewListRepository repository) {
        this.repository = repository;
    }

    public ISmallPreviewListRepository getRepository() {
        return repository;
    }
}
