import java.util.HashMap;
import java.util.function.DoubleConsumer;

/**
 * A finite-state machine. It contains a dictionary that associates names with lambda function behaviors. 
 * It allows any class with an FSM to define a set of states and transition between them.
 */
public class FSM {
	private String activeState;
    private HashMap<String, DoubleConsumer> stateFunctions = new HashMap<String, DoubleConsumer>();
    
	public FSM() {
        activeState = null;
    }

    /**
     * Updates the current state
     * @param state
     */
	public void setState(String state) {
		activeState = state;
	}

    /**
     * Runs the behavior associated with the current active state.
     */
	public void update(double dt) {
        if(activeState != null) {
            if(stateFunctions.get(activeState) != null) {
                stateFunctions.get(activeState).accept(dt);
            }
        }
	}

    /**
     * Add a new state to the FSM's dictionary.
     * @param name
     * @param func
     */
    public void defineState(String name, DoubleConsumer func) {
        stateFunctions.put(name, func);
    }
}
