package infrastructure;

import ports.driven.ServersRepository;

import java.util.Collections;

class InMemoryServersRepositoryAdapterTest extends RepositoryAdapaterContract {
    @Override
    protected ServersRepository createRepository() {
        return new InMemoryServersRepositoryAdapter(Collections.emptyList());
    }
}