package org.meteorminer.network;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.LongPollAdaptor;
import org.meteorminer.binding.Authorization;

import javax.inject.Inject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author John Ericksen
 */
public class JsonClient {

    @Inject
    @Authorization
    private String userPassword;
    @Inject
    private BitcoinConnectionFactory connectionFactory;
    @Inject
    private ObjectMapper mapper;
    @Inject
    private LongPollAdaptor longPollAdaptor;

    public JsonNode execute(String requestMessage, URL url) throws IOException {
        return execute(requestMessage, connectionFactory.getBitcoinConnection(url));
    }

    public JsonNode execute(String requestMessage) throws IOException {

        HttpURLConnection connection = connectionFactory.getBitcoinConnection();
        return execute(requestMessage, connection);
    }

    public JsonNode execute(String requestMessage, HttpURLConnection connection) throws IOException {

        connection.setRequestProperty("Authorization", userPassword);
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream requestStream = connection.getOutputStream();
        Writer request = new OutputStreamWriter(requestStream);
        request.write(requestMessage);

        request.close();
        requestStream.close();

        ObjectNode responseMessage = null;

        InputStream responseStream = null;

        try {
            longPollAdaptor.setupLongpoll(connection);

            responseStream = unboxStream(connection, new StreamExecution() {
                public InputStream getInputStream(HttpURLConnection connection) throws IOException {
                    return connection.getInputStream();
                }
            });

            if (responseStream == null)
                throw new IOException("Drop to error handler");

            responseMessage = (ObjectNode) mapper.readTree(responseStream);

        } catch (JsonProcessingException e) {
            throw new IOException("Bitcoin returned unparsable JSON");
        } catch (IOException e) {
            readError(connection);
        } finally {
            if (responseStream != null) {
                responseStream.close();
            }
        }

        if (responseMessage == null || !responseMessage.has("result"))
            throw new IOException("Bitcoin did not return a result or an error");


        return responseMessage.get("result");
    }

    private void readError(HttpURLConnection connection) throws IOException {
        if (connection.getErrorStream() == null)
            throw new IOException("Bitcoin disconnected during response: "
                    + connection.getResponseCode() + " " + connection.getResponseMessage());

        InputStream errorStream = unboxStream(connection, new StreamExecution() {
            public InputStream getInputStream(HttpURLConnection connection) {
                return connection.getErrorStream();
            }
        });

        if (errorStream == null)
            throw new IOException("Bitcoin disconnected during response: "
                    + connection.getResponseCode() + " " + connection.getResponseMessage());


        String error = IOUtils.toString(errorStream);

        if (error.startsWith("{")) {
            try {
                checkForError((ObjectNode) mapper.readTree(error));
            } catch (JsonProcessingException f) {
                throw new IOException("Bitcoin returned unparsable JSON");
            }
        } else {
            throw new IOException("Bitcoin returned error message: " + error);
        }

        errorStream.close();

        throw new IOException("Bitcoin returned an error, but with no message");
    }

    private void checkForError(ObjectNode objectNode) throws IOException {
        if (objectNode.has("error")) {
            if (objectNode.get("error").has("message")) {
                String error = objectNode.get("error").get("message").getValueAsText().trim();
                throw new IOException("Bitcoin returned error message: " + error);
            } else if (objectNode.has("error")) {
                String error = objectNode.get("error").getValueAsText().trim();

                if (!"null".equals(error) && !"".equals(error))
                    throw new IOException("Bitcoin returned error message: " + error);
            }
        }
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

    private interface StreamExecution {
        InputStream getInputStream(HttpURLConnection connection) throws IOException;
    }
}
