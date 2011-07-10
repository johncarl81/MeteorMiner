package org.meteorminer.network;

import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author John Ericksen
 */
public class JsonClient {

    @Inject
    private BitcoinConnectionFactory connectionFactory;
    @Inject
    private CLInterface output;
    @Inject
    private Set<RPCExtension> extensions;

    public <T> T execute(URL url, MessageStrategy<T> messageStrategy) throws IOException {
        return execute(connectionFactory.getBitcoinConnection(url), messageStrategy);
    }

    public <T> T execute(MessageStrategy<T> messageStrategy) throws IOException {

        HttpURLConnection connection = connectionFactory.getBitcoinConnection();
        return execute(connection, messageStrategy);
    }

    public <T> T execute(HttpURLConnection connection, MessageStrategy<T> messageStrategy) throws IOException {

        output.verbose("JSON Request " + messageStrategy.getRequestType() + " @ " + connection.getURL());

        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream requestStream = connection.getOutputStream();
        Writer request = new OutputStreamWriter(requestStream);
        request.write(messageStrategy.getRequestMessage());

        request.close();
        requestStream.close();

        T response = null;

        InputStream responseStream = null;

        try {
            for (RPCExtension extension : extensions) {
                extension.setup(connection);
            }

            responseStream = unboxStream(connection, new StreamExecution() {
                public InputStream getInputStream(HttpURLConnection connection) throws IOException {
                    return connection.getInputStream();
                }
            });

            if (responseStream == null) {
                connectionFactory.errorUpdate();
                throw new IOException("Drop to error handler");
            }

            response = messageStrategy.getResponseFactory().buildResponse(responseStream);

        } catch (IOException e) {
            connectionFactory.errorUpdate();
            throw new IOException("Error while recieving response from server", e);
        } finally {
            if (responseStream != null) {
                responseStream.close();
            }
        }

        if (response == null) {
            connectionFactory.errorUpdate();
            throw new IOException("Bitcoin did not return a result or an error");
        }

        return response;
    }

    private InputStream unboxStream(HttpURLConnection connection, StreamExecution execution) throws IOException {
        if (connection.getContentEncoding() != null) {
            if (connection.getContentEncoding().equalsIgnoreCase("gzip"))
                return new GZIPInputStream(execution.getInputStream(connection));
            else if (connection.getContentEncoding().equalsIgnoreCase("deflate"))
                return new InflaterInputStream(execution.getInputStream(connection));
        }
        return execution.getInputStream(connection);
    }

    public void setExtensions(Set<RPCExtension> extensions) {
        this.extensions = extensions;
    }

    private interface StreamExecution {
        InputStream getInputStream(HttpURLConnection connection) throws IOException;
    }
}
