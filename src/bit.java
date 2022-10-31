
public class bit {

	private boolean value;

	// sets the value of the bit
	public void set(boolean value) {
		this.value = value;
	}

	// changes the value from true to false or false to true
	public void toggle() {
		if (value == false) {
			value = true;
		} else {
			value = false;
		}
	}

	// sets the bit to true
	public void set() {
		value = true;
	}

	// sets the bit to false
	public void clear() {
		value = false;
	}

	// returns the current value
	public boolean getValue() {
		return value;
	}

	// performs AND on two bits and returns a new bit 'answer'
	public bit and(bit other) {
		bit answer = new bit(); // create a new bit for 'answer'
		if (value == false) { // if any values are false, set to false
			answer.clear();
		} else {
			if (other.getValue() == false)
				answer.clear();
			else // if both values are true, set to true
				answer.set();
		}
		return answer;
	}

	// performs OR on two bits and returns a new bit 'answer'
	public bit or(bit other) {
		bit answer = new bit(); // create a new bit 'answer'
		if (value == true) { // if any one of the values are true, set to true
			answer.set();
		} else if (other.getValue() == true) {
			answer.set();
		} else { // If both values are false, set to false
			answer.clear();
		}

		return answer;
	}

	// performs XOR on two bits and returns a new bit 'answer'
	public bit xor(bit other) {
		bit answer = new bit(); // create a new bit 'answer'
		if (value == true) { // if one of the bit's values are true, set to true
			if (other.getValue() == false) {
				answer.set();
			} else { // else set it to false
				answer.clear();
			}
		} else if (other.getValue() == true) {
			answer.set();
		} else {
			answer.clear();
		}
		return answer;
	}

	// performs NOT on the existing bit, returning the result as a new bit
	public bit not() {
		if (value == false) {
			set();
		} else {
			clear();
		}
		return this;
	}

	// returns “t” or “f”
	@Override
	public String toString() {
		if (value == true) {
			return "1";
		} else {
			return "0";
		}

	}
}
