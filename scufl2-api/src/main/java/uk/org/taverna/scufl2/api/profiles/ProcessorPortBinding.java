package uk.org.taverna.scufl2.api.profiles;

import uk.org.taverna.scufl2.api.common.AbstractCloneable;
import uk.org.taverna.scufl2.api.common.Child;
import uk.org.taverna.scufl2.api.common.Visitor;
import uk.org.taverna.scufl2.api.common.WorkflowBean;
import uk.org.taverna.scufl2.api.port.ActivityPort;
import uk.org.taverna.scufl2.api.port.InputActivityPort;
import uk.org.taverna.scufl2.api.port.InputProcessorPort;
import uk.org.taverna.scufl2.api.port.OutputActivityPort;
import uk.org.taverna.scufl2.api.port.OutputProcessorPort;
import uk.org.taverna.scufl2.api.port.ProcessorPort;

/**
 * The binding between an <code>ActivityPort</code> and a
 * <code>ProcessorPort</code>.
 * <p>
 * This abstract class is realized as either an
 * {@link ProcessorInputPortBinding} or {@link ProcessorOutputPortBinding}. For
 * an input port binding, the binding goes from an {@link InputProcessorPort} to
 * an {@link InputActivityPort}, while for an output port binding the binding
 * goes from an {@link OutputActivityPort} to an {@link OutputProcessorPort}.
 * 
 * @author Alan R Williams
 * @author Stian Soiland-Reyes
 * @param <A>
 *            the <code>ActivityPort</code>
 * @param <P>
 *            the <code>ProcessorPort</code>
 *            
 */
public abstract class ProcessorPortBinding<A extends ActivityPort, P extends ProcessorPort>
		extends AbstractCloneable implements Child<ProcessorBinding> {

	private P boundProcessorPort;
	private A boundActivityPort;

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * Return the {@link ActivityPort} which is passing data from/to the
	 * {@link #getBoundProcessorPort()}.
	 * 
	 * @return the <code>ActivityPort</code> to which data is passing from/to
	 *         the bound <code>ProcessorPort</code>
	 */
	public A getBoundActivityPort() {
		return boundActivityPort;
	}

	/**
	 * Return the {@link ProcessorPort} which is passing data to/from the
	 * {@link #getBoundActivityPort()}.
	 * 
	 * @return the <code>ProcessorPort</code> to which data is passing to/from
	 *         the bound <code>ActivityPort</code>
	 */
	public P getBoundProcessorPort() {
		return boundProcessorPort;
	}

	/**
	 * Sets the {@link ActivityPort} which is passing data from/to the
	 * {@link #getBoundProcessorPort()}.
	 * 
	 * @param boundActivityPort
	 *            the <code>ActivityPort</code> to which data is passing from/to
	 *            the bound <code>ProcessorPort</code>
	 */
	public void setBoundActivityPort(A boundActivityPort) {
		this.boundActivityPort = boundActivityPort;
	}

	/**
	 * Sets the {@link ProcessorPort} which is passing data to/from the
	 * {@link #getBoundActivityPort()}.
	 * 
	 * @param boundProcessorPort
	 *            the <code>ProcessorPort</code> to which data is passing
	 *            to/from the bound <code>ActivityPort</code>
	 */
	public void setBoundProcessorPort(P boundProcessorPort) {
		this.boundProcessorPort = boundProcessorPort;
	}

	@Override
	protected void cloneInto(WorkflowBean clone, Cloning cloning) {
		@SuppressWarnings("unchecked")
		ProcessorPortBinding<A, P> cloneBinding = (ProcessorPortBinding<A, P>) clone;
		cloneBinding.setBoundActivityPort(cloning
				.cloneOrOriginal(getBoundActivityPort()));
		cloneBinding.setBoundProcessorPort(cloning
				.cloneOrOriginal(getBoundProcessorPort()));
	}

}
