/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcap;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;

/**
 *
 * @author sergey vetyutnev
 * 
 */
public class PrevewDialogData {
	private ApplicationContextName lastACN;
	private InvokeImpl[] operationsSentA;
	private InvokeImpl[] operationsSentB;

	private Object upperDialog;

	private TCAPProviderImpl.PrevewDialogDataKey prevewDialogDataKey1;
	private TCAPProviderImpl.PrevewDialogDataKey prevewDialogDataKey2;

	private ReentrantLock dialogLock = new ReentrantLock();
	private Future idleTimerFuture;
	private ScheduledExecutorService executor;
	private TCAPProviderImpl provider;
	private long idleTaskTimeout;

	public PrevewDialogData(TCAPProviderImpl provider) {
		this.provider = provider;
		TCAPStack stack = provider.getStack();
		this.idleTaskTimeout = stack.getDialogIdleTimeout();
		this.executor = provider._EXECUTOR;
	}

	public ApplicationContextName getLastACN() {
		return lastACN;
	}

	public InvokeImpl[] getOperationsSentA() {
		return operationsSentA;
	}

	public InvokeImpl[] getOperationsSentB() {
		return operationsSentB;
	}

	public Object getUpperDialog() {
		return upperDialog;
	}

	public void setLastACN(ApplicationContextName val) {
		lastACN = val;
	}

	public void setOperationsSentA(InvokeImpl[] val) {
		operationsSentA = val;
	}

	public void setOperationsSentB(InvokeImpl[] val) {
		operationsSentB = val;
	}

	public void setUpperDialog(Object val) {
		upperDialog = val;
	}

	protected TCAPProviderImpl.PrevewDialogDataKey getPrevewDialogDataKey1() {
		return prevewDialogDataKey1;
	}

	protected TCAPProviderImpl.PrevewDialogDataKey getPrevewDialogDataKey2() {
		return prevewDialogDataKey2;
	}

	protected void setPrevewDialogDataKey1(TCAPProviderImpl.PrevewDialogDataKey val) {
		prevewDialogDataKey1 = val;
	}

	protected void setPrevewDialogDataKey2(TCAPProviderImpl.PrevewDialogDataKey val) {
		prevewDialogDataKey2 = val;
	}

	protected void startIdleTimer() {

		try {
			this.dialogLock.lock();
			if (this.idleTimerFuture != null) {
				throw new IllegalStateException();
			}

			IdleTimerTask t = new IdleTimerTask();
			t.pdd = this;
			this.idleTimerFuture = this.executor.schedule(t, this.idleTaskTimeout, TimeUnit.MILLISECONDS);

		} finally {
			this.dialogLock.unlock();
		}
	}

	protected void stopIdleTimer() {
		try {
			this.dialogLock.lock();
			if (this.idleTimerFuture != null) {
				this.idleTimerFuture.cancel(false);
				this.idleTimerFuture = null;
			}

		} finally {
			this.dialogLock.unlock();
		}
	}

	protected void restartIdleTimer() {
		stopIdleTimer();
		startIdleTimer();
	}

	private class IdleTimerTask implements Runnable {
		PrevewDialogData pdd;

		public void run() {
			try {
				dialogLock.lock();

				provider.removePreviewDialog(pdd);
			} finally {
				dialogLock.unlock();
			}
		}

	}
}