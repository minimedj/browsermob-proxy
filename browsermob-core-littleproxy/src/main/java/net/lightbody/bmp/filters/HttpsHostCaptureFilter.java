package net.lightbody.bmp.filters;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;

/**
 * Captures the host for HTTPS requests and stores the value in the ChannelHandlerContext for use by {@link HttpsAwareFiltersAdapter}
 * filters. This filter reads the host from the HttpRequest during the HTTP CONNECT call, and therefore MUST be invoked
 * after any other filters which modify the host.
 */
public class HttpsHostCaptureFilter extends HttpFiltersAdapter {
    public HttpsHostCaptureFilter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        if (httpObject instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) httpObject;

            if (ProxyUtils.isCONNECT(httpRequest)) {
                Attribute<String> hostname = ctx.attr(AttributeKey.<String>valueOf(HttpsAwareFiltersAdapter.HOST_ATTRIBUTE_NAME));
                String hostAndPort = httpRequest.getUri();
                hostname.set(hostAndPort);
            }
        }

        return null;
    }
}
