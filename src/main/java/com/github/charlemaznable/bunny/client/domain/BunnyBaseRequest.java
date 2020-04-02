package com.github.charlemaznable.bunny.client.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.charlemaznable.core.net.common.CncRequest;
import com.github.charlemaznable.core.net.common.PathVar;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static org.joor.Reflect.onClass;

@Getter
public abstract class BunnyBaseRequest<T extends BunnyBaseResponse> implements CncRequest<T> {

    @JSONField(serialize = false, deserialize = false)
    @PathVar("bunny-address")
    protected String bunnyAddress;
    /**
     * 服务名称
     */
    @Setter
    private String serveName;
    /**
     * 可选扩展参数
     */
    private Map<String, Object> context = newHashMap();

    public T createResponse() {
        T response = onClass(responseClass()).create().get();
        response.setServeName(this.serveName);
        return response;
    }
}
