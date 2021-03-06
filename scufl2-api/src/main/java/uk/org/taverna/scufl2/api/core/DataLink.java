package uk.org.taverna.scufl2.api.core;

import uk.org.taverna.scufl2.api.common.AbstractCloneable;
import uk.org.taverna.scufl2.api.common.Child;
import uk.org.taverna.scufl2.api.common.Visitor;
import uk.org.taverna.scufl2.api.common.WorkflowBean;
import uk.org.taverna.scufl2.api.impl.NullSafeComparator;
import uk.org.taverna.scufl2.api.port.Port;
import uk.org.taverna.scufl2.api.port.ReceiverPort;
import uk.org.taverna.scufl2.api.port.SenderPort;

/**
 * A <code>DataLink</code> controls the flow of data in a {@link Workflow}.
 * <p>
 * <code>DataLink</code>s receive data from a {@link SenderPort} and send the data to a
 * {@link ReceiverPort}. More than one <code>DataLink</code> may receive data from the same
 * <code>SenderPort</code> and more than one <code>DataLink</code> may send data to the same
 * <code>ReceiverPort</code>.
 * <p>
 * If more than one <code>DataLink</code> sends data to the same <code>ReceiverPort</code> each
 * <code>DataLink</code> must specify its merge position. If only one <code>DataLink</code> sends
 * data to a <code>ReceiverPort</code> and there is no merge then the merge position must be set to
 * <code>null</code>.
 * 
 * @author Alan R Williams
 */
@SuppressWarnings("rawtypes")
public class DataLink extends AbstractCloneable implements Child<Workflow>, Comparable {

	private ReceiverPort sendsTo;

	private SenderPort receivesFrom;

	private Integer mergePosition;

	private Workflow parent;

	/**
	 * Constructs an unconnected <code>DataLink</code>.
	 */
	public DataLink() {
		super();
	}

	/**
	 * Constructs a <code>DataLink</code> with the specified parent {@link Workflow} that connects
	 * the specified {@link SenderPort} and {@link ReceiverPort}.
	 * 
	 * @param parent
	 *            the <code>Workflow</code> to set as the <code>DataLink</code>'s parent. Can be
	 *            <code>null</code>.
	 * @param senderPort
	 *            the <code>SenderPort</code> that the <code>DataLink</code> receives data from. Can
	 *            be <code>null</code>.
	 * @param receiverPort
	 *            the <code>ReceiverPort</code> that the <code>DataLink</code> sends data to. Can be
	 *            <code>null</code>.
	 */
	public DataLink(Workflow parent, SenderPort senderPort, ReceiverPort receiverPort) {
		setReceivesFrom(senderPort);
		setSendsTo(receiverPort);
		setParent(parent);
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(Object other) {
		if (getClass() != other.getClass()) {
			int classCompare = getClass().getCanonicalName().compareTo(
					other.getClass().getCanonicalName());
			if (classCompare != 0) {
				// Allow having say InputPorts and OutputPorts in the same sorted list
				return classCompare;
			}
		}
		DataLink o1 = this;
		DataLink o2 = (DataLink) other;
		
		int senderCompare = portCompare(o1.getReceivesFrom(), o2.getReceivesFrom());
		if (senderCompare != 0) {
			return senderCompare;
		}

		int receiverCompare = portCompare(o1.getSendsTo(), o2.getSendsTo());
		if (receiverCompare != 0) {
			return receiverCompare;
		}
		return NullSafeComparator.compareObjects(o1.getMergePosition(), o2.getMergePosition());
	}

		
	@SuppressWarnings("unchecked")
	private int portCompare(Port a, Port b) {
		Integer nullCompare = NullSafeComparator.nullCompare(a, b);
		if (nullCompare != null) {
			return nullCompare;
		}

		// All known Port implementations are also Child instances
		WorkflowBean aParent = ((Child)a).getParent();
		WorkflowBean bParent = ((Child)b).getParent();
		int parentCompare = NullSafeComparator.compareObjects(aParent, bParent);	
		if (parentCompare != 0) {
			return parentCompare;
		}
		// OK, just to boring Port compare then
		return a.compareTo(b);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DataLink other = (DataLink) obj;
		if (getSendsTo() == null) {
			if (other.getSendsTo() != null) {
				return false;
			}
		} else if (!getSendsTo().equals(other.getSendsTo())) {
			return false;
		}
		if (getReceivesFrom() == null) {
			if (other.getReceivesFrom() != null) {
				return false;
			}
		} else if (!getReceivesFrom().equals(other.getReceivesFrom())) {
			return false;
		}		
		if (getMergePosition() == null) {
			if (other.getMergePosition() != null) {
				return false;
			}
		} else if (!getMergePosition().equals(other.getMergePosition())) {
			return false;
		}
		
		
		return true;
	}

	/**
	 * Returns the position that this <code>DataLink</code> should join a merge.
	 * 
	 * If there is no merge then the value must be <code>null</code>.
	 * <p>
	 * When more than one <code>DataLink</code> sends the data to the same {@link ReceiverPort} then
	 * each <code>DataLink</code> must specify its merge position. Merge positions must be
	 * sequential staring from zero.
	 * <p>
	 * For a merge with only one <code>DataLink</code> input the merge position is zero.
	 * 
	 * @return the position that this <code>DataLink</code> should join a merge
	 */
	public Integer getMergePosition() {
		return mergePosition;
	}

	/**
	 * Returns the parent <code>Workflow</code> of null if this <code>DataLink</code> is an orphan.
	 * 
	 * @return the parent <code>Workflow</code> of null if this <code>DataLink</code> is an orphan
	 */
	@Override
	public Workflow getParent() {
		return parent;
	}

	/**
	 * Returns the <code>SenderPort</code> that this <code>DataLink</code> receives data from.
	 * 
	 * @return the <code>SenderPort</code> that this <code>DataLink</code> receives data from
	 */
	public SenderPort getReceivesFrom() {
		return receivesFrom;
	}

	/**
	 * Returns the <code>ReceiverPort</code> that this <code>DataLink</code> sends data to.
	 * 
	 * @return the <code>ReceiverPort</code> that this <code>DataLink</code> sends data to
	 */
	public ReceiverPort getSendsTo() {
		return sendsTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getSendsTo() == null ? 0 : getSendsTo().hashCode());
		result = prime * result + (getReceivesFrom() == null ? 0 : getReceivesFrom().hashCode());
		return result;
	}

	/**
	 * Sets the position that this <code>DataLink</code> should join a merge.
	 * 
	 * If there is no merge then the value must be set to <code>null</code>.
	 * <p>
	 * When more than one <code>DataLink</code> sends the data to the same {@link ReceiverPort} then
	 * each <code>DataLink</code> must specify its merge position. Merge positions must be
	 * sequential staring from zero.
	 * <p>
	 * For a merge with only one <code>DataLink</code> input the merge position is set to zero.
	 * 
	 * @param mergePosition
	 *            the position that this <code>DataLink</code> should join a merge. Can be null.
	 */
	public void setMergePosition(Integer mergePosition) {
		this.mergePosition = mergePosition;
	}

	@Override
	public void setParent(Workflow parent) {
		if (this.parent != null && this.parent != parent) {
			this.parent.getDataLinks().remove(this);
		}
		this.parent = parent;
		if (parent != null) {
			parent.getDataLinks().add(this);
		}
	}

	/**
	 * Returns the <code>SenderPort</code> that this <code>DataLink</code> receives data from.
	 * 
	 * @param receivesFrom
	 *            the <code>SenderPort</code> that this <code>DataLink</code> receives data from
	 */
	public void setReceivesFrom(SenderPort receivesFrom) {
		this.receivesFrom = receivesFrom;
	}

	/**
	 * Sets the <code>ReceiverPort</code> that this <code>DataLink</code> sends data to.
	 * 
	 * @param sendsTo
	 *            the <code>ReceiverPort</code> that this <code>DataLink</code> sends data to
	 */
	public void setSendsTo(ReceiverPort sendsTo) {
		this.sendsTo = sendsTo;
	}

	@Override
	public String toString() {
		return String.format("%s %s=>%s", getClass().getSimpleName(),
				getReceivesFrom() != null ? getReceivesFrom().getName() : "",
				getSendsTo() != null ? getSendsTo().getName() : "");
	}
	
	@Override
	protected void cloneInto(WorkflowBean clone, Cloning cloning) {
		DataLink cloneLink = (DataLink)clone;
		cloneLink.setMergePosition(getMergePosition());
		cloneLink.setReceivesFrom(cloning.cloneIfNotInCache(getReceivesFrom()));
		cloneLink.setSendsTo(cloning.cloneIfNotInCache(getSendsTo()));
	}

}
