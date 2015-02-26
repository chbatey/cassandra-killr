package info.batey.cassandra.breaker;

import org.apache.cassandra.cql3.*;
import org.apache.cassandra.cql3.statements.BatchStatement;
import org.apache.cassandra.cql3.statements.ParsedStatement;
import org.apache.cassandra.exceptions.ReadTimeoutException;
import org.apache.cassandra.exceptions.RequestExecutionException;
import org.apache.cassandra.exceptions.RequestValidationException;
import org.apache.cassandra.service.QueryState;
import org.apache.cassandra.transport.messages.ResultMessage;
import org.apache.cassandra.utils.MD5Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorator around the real query handler that does crazy things.
 */
public class CrazyQueryHandler implements QueryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrazyQueryHandler.class);

    private final PrimingServer primingServer;
    private QueryHandler realQueryHandler = QueryProcessor.instance;

    public CrazyQueryHandler() {
        primingServer = new PrimingServer();
        primingServer.start();
    }

    @Override
    public ResultMessage process(String query, QueryState state, QueryOptions options) throws RequestExecutionException, RequestValidationException {
        if (PrimingServer.isReadtimeout()) {
            LOGGER.info("Read time out hahahah");
            throw new ReadTimeoutException(options.getConsistency(), 1, 1, false);
        } else {
            LOGGER.info("Delegating to real Cassandra");
            return realQueryHandler.process(query, state, options);
        }
    }

    @Override
    public ResultMessage.Prepared prepare(String query, QueryState state) throws RequestValidationException {
        return realQueryHandler.prepare(query, state);
    }

    @Override
    public ParsedStatement.Prepared getPrepared(MD5Digest id) {
        return realQueryHandler.getPrepared(id);
    }

    @Override
    public ParsedStatement.Prepared getPreparedForThrift(Integer id) {
        return realQueryHandler.getPreparedForThrift(id);
    }

    @Override
    public ResultMessage processPrepared(CQLStatement statement, QueryState state, QueryOptions options) throws RequestExecutionException, RequestValidationException {
        return realQueryHandler.processPrepared(statement, state, options);
    }

    @Override
    public ResultMessage processBatch(BatchStatement statement, QueryState state, BatchQueryOptions options) throws RequestExecutionException, RequestValidationException {
        return realQueryHandler.processBatch(statement, state, options);
    }
}
