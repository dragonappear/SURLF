package me.dragonappear.domain.statics;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.statics.dto.LinkStaticsDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LinkStaticsService {

    private final ElasticsearchClient elasticsearchClient;

    public List<LinkStaticsDto> getLinkStaticsPer7days(String shortId) throws IOException {

        SearchResponse<Object> search = elasticsearchClient.search(s -> s
                .index("short-link-log-*")
                .query(q -> q.match(m -> m.field("shortId").query(shortId))).size(0)
                .aggregations(Map.of("index_counts", AggregationBuilders.terms(a -> a.field("_index")))).size(7), Object.class);

        StringTermsAggregate aggregate = search.aggregations().get("index_counts").sterms();

        return aggregate.buckets().array().stream().map(bucket -> {

            String stringValue = bucket.key().stringValue(); // short-link-log-2023.04.10
            int idx = bucket.key().stringValue().lastIndexOf("-");

            String date = stringValue.substring(idx + 1); // 2023.04.10
            String docCount = String.valueOf(bucket.docCount());

            return new LinkStaticsDto(date, docCount);

        }).toList();
    }
}
