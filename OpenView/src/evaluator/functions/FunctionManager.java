/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluator.functions;

import java.util.ArrayList;
import java.util.HashMap;

import evaluator.functions.array.ArrayAdd;
import evaluator.functions.array.ArrayFind;
import evaluator.functions.array.ArrayGet;
import evaluator.functions.array.ArrayLength;
import evaluator.functions.array.ArrayRemove;
import evaluator.functions.converters.ToString;
import evaluator.functions.math.Abs;
import evaluator.functions.math.Acos;
import evaluator.functions.math.Asin;
import evaluator.functions.math.Atan;
import evaluator.functions.math.Atan2;
import evaluator.functions.math.Cos;
import evaluator.functions.math.Exp;
import evaluator.functions.math.Log;
import evaluator.functions.math.Max;
import evaluator.functions.math.Min;
import evaluator.functions.math.Pow;
import evaluator.functions.math.Rand;
import evaluator.functions.math.Round;
import evaluator.functions.math.Sin;
import evaluator.functions.math.Sqrt;
import evaluator.functions.math.Tan;
import evaluator.functions.utils.TypeOf;

/**
 * Static class to manage the list of {@link Function}
 * 
 * @author martino
 */
public class FunctionManager {
	/***
	 * List of {@link Function}
	 */
	private static ArrayList<Function> functions_;
	/***
	 * Name/Function {@link HashMap}
	 */
	private static HashMap<String, Function> functionsMap_;

	/***
	 * Initialize standard functions
	 */
	public static void initFunctions() {
		functions_ = new ArrayList<>();
		// Math
		functions_.add(new Abs());
		functions_.add(new Acos());
		functions_.add(new Asin());
		functions_.add(new Atan());
		functions_.add(new Atan2());
		// functions_.add(new Ceil());
		functions_.add(new Cos());
		functions_.add(new Exp());
		// functions_.add(new Floor());
		// functions_.add(new Limit());
		functions_.add(new Log());
		functions_.add(new Max());
		// functions_.add(new Mean());
		functions_.add(new Min());
		functions_.add(new Pow());
		functions_.add(new Rand());
		// functions_.add(new Rint());
		functions_.add(new Round());
		functions_.add(new Sin());
		functions_.add(new Sqrt());
		functions_.add(new Tan());
		// functions_.add(new ToDegrees());
		// functions_.add(new ToRadians());

		functions_.add(new ToString());

		functions_.add(new ArrayGet());
		functions_.add(new ArrayAdd());
		functions_.add(new ArrayRemove());
		functions_.add(new ArrayLength());
		functions_.add(new ArrayFind());
		
		functions_.add(new TypeOf());
	}

	/***
	 * Retrieve the Name/Function {@link HashMap}
	 * 
	 * @return Name/Function {@link HashMap}
	 */
	public static HashMap<String, Function> getFunctionMap() {
		if (functionsMap_ == null) {
			functionsMap_ = new HashMap<>();

			for (Function o : getFunctions()) {
				functionsMap_.put(o.name(), o);
			}

		}
		return functionsMap_;
	}

	/***
	 * Get a {@link Function} from a given name
	 * 
	 * @param key
	 *            Name of the function
	 * @return {@link Function} (if any)
	 */
	public static Function get(String key) {
		return getFunctionMap().get(key);
	}

	/***
	 * Add extra {@link Function} to the list
	 * 
	 * @param f
	 *            {@link Function} to add
	 */
	public static void addFunction(Function f) {
		if (functionsMap_ != null) {
			functionsMap_.clear();
			functionsMap_ = null;
		}
		functions_.add(f);
	}

	/***
	 * Add a list of extra {@link Function}s to the list
	 * 
	 * @param fs
	 *            list of {@link Function}s to add
	 */
	public static void addFunctions(ArrayList<Function> fs) {
		if (functionsMap_ != null) {
			functionsMap_.clear();
			functionsMap_ = null;
		}
		for (Function f : fs) {
			functions_.add(f);
		}
	}

	/***
	 * Get the list of {@link Function}s declared
	 * 
	 * @return list of {@link Function}s
	 */
	public static ArrayList<Function> getFunctions() {
		if (functions_ == null) {
			initFunctions();
		}
		return functions_;
	}
}
