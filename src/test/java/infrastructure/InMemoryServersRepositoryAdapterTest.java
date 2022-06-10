package infrastructure;

import ports.driven.ServersRepository;

class InMemoryServersRepositoryAdapterTest extends RepositoryAdapterContract {
    @Override
    protected ServersRepository createRepository() {
        return new InMemoryServersRepositoryAdapter();
    }
}