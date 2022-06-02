package infrastructure;

import ports.driven.ServersRepository;

import java.util.Collections;

class InMemoryServersRepositoryAdapterTest extends RepositoryAdapterContract {
    @Override
    protected ServersRepository createRepository() {
        return new InMemoryServersRepositoryAdapter(Collections.emptyList());
    }
}