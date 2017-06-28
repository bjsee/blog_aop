package blog.aop;

import blog.aop.errorhandling.ErrorHandler;
import blog.aop.security.SecurityService;
import blog.aop.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
class SuperService {
	private static final Logger LOG = LoggerFactory.getLogger(SuperService.class);

	@Autowired
	private PlatformTransactionManager txMgr;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private SuperDao dao;

	public void complexOperation(ComplexObject obj) {
		final long start = System.currentTimeMillis();
		try {
			final User user = securityService.getUserFromCurrentSession();
			if (!securityService.isAuthorized(user, "SuperService.complexOperation")) {
				throw new UserNotAuthorizedException();
			}

			if (LOG.isTraceEnabled()) {
				LOG.trace("Start" + SuperService.class + ".complexOperation()");
			}
			final TransactionStatus tx = txMgr.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			try {
				calculate(obj);
				dao.save(obj);

				txMgr.commit(tx);
			} finally {
				if (!tx.isCompleted()) {
					txMgr.rollback(tx);
				}
			}
		} catch (final Throwable t) {
			errorHandler.handleError();
		} finally {
			final long took = System.currentTimeMillis() - start;
			LOG.info(SuperService.class + ".complexOperation() took" + took + "ms");
		}
	}

	private void calculate(ComplexObject obj) {
	}
}
