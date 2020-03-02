package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 余额查询接口 请求
 */
@Getter
@Setter
public class QueryRequest extends BunnyBaseRequest<QueryResponse> {

    public QueryRequest() {
        this.bunnyAddress = BunnyAddress.QUERY;
    }

    @Override
    public Class<QueryResponse> responseClass() {
        return QueryResponse.class;
    }
}
