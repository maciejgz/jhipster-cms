package pl.mg.jhipster.cms.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.mg.jhipster.cms.domain.Project;
import pl.mg.jhipster.cms.repository.ProjectRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Project} entity.
 */
public interface ProjectSearchRepository extends ElasticsearchRepository<Project, Long>, ProjectSearchRepositoryInternal {}

interface ProjectSearchRepositoryInternal {
    Stream<Project> search(String query);

    Stream<Project> search(Query query);

    void index(Project entity);
}

class ProjectSearchRepositoryInternalImpl implements ProjectSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ProjectRepository repository;

    ProjectSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, ProjectRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Project> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Project> search(Query query) {
        return elasticsearchTemplate.search(query, Project.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Project entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
