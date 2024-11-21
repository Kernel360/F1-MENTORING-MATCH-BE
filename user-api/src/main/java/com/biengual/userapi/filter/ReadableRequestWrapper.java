package com.biengual.userapi.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ReadableRequestWrapper extends HttpServletRequestWrapper {

    private final Charset encoding;
    private byte[] rawData;
    private String requestBody;

    public ReadableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        String charEncoding = request.getCharacterEncoding();
        this.encoding = StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        InputStream inputStream = request.getInputStream();
        this.rawData = IOUtils.toByteArray(inputStream);

        this.requestBody = new String(this.rawData, this.encoding);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream buffer = new ByteArrayInputStream(rawData);
        return new ServletInputStreamImpl(buffer);
    }

    public String getRequestBody() {
        return this.requestBody;
    }

    @AllArgsConstructor
    private static class ServletInputStreamImpl extends ServletInputStream {
        private final ByteArrayInputStream buffer;

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() {
            return buffer.read();
        }
    }
}
