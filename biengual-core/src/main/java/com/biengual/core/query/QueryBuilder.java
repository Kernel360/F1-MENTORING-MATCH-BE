package com.biengual.core.query;

import java.util.List;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.RangeQuery;

public class QueryBuilder {

    /**
     * Match 쿼리를 생성합니다.
     *
     * @param field 필드 이름
     * @param value 검색할 값
     * @return MatchQuery 객체
     */
    public static Query matchQuery(String field, Object value) {
        return new MatchQuery.Builder()
            .field(field)
            .query((FieldValue)value)
            .build()
            .toQuery();
    }

    /**
     * Range 쿼리를 생성합니다.
     *
     * @param field 필드 이름
     * @param from 시작 값
     * @param to 종료 값
     * @return RangeQuery 객체
     */
    public static Query rangeQuery(String field, Object from, Object to) {
        return new RangeQuery.Builder()
            .field(field)
            .from((JsonData)from)
            .to((JsonData)to)
            .build()
            .toQuery();
    }

    /**
     * Bool 쿼리를 생성합니다.
     *
     * @param mustQueries 필수 조건 리스트
     * @param shouldQueries 선택 조건 리스트
     * @param mustNotQueries 제외 조건 리스트
     * @return BoolQuery 객체
     */
    public static Query boolQuery(List<Query> mustQueries, List<Query> shouldQueries, List<Query> mustNotQueries) {
        return new BoolQuery.Builder()
            .must(mustQueries)
            .should(shouldQueries)
            .mustNot(mustNotQueries)
            .minimumShouldMatch("1") // 최소 하나의 should 조건 만족
            .build()
            .toQuery();
    }
}