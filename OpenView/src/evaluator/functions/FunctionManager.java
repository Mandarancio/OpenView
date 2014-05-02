/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluator.functions;

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
import evaluator.functions.math.Sin;
import evaluator.functions.math.Sqrt;
import evaluator.functions.math.Tan;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author martino
 */
public class FunctionManager {

    private ArrayList<Function> functions_ = new ArrayList<>();
    private HashMap<String, Function> functionsMap_;
    
    public FunctionManager(){
          // Math
        functions_.add(new Abs());
        functions_.add(new Acos());
        functions_.add(new Asin());
        functions_.add(new Atan());
        functions_.add(new Atan2());
//        functions_.add(new Ceil());
        functions_.add(new Cos());
        functions_.add(new Exp());
//        functions_.add(new Floor());
//        functions_.add(new Limit());
        functions_.add(new Log());
        functions_.add(new Max());
//        functions_.add(new Mean());
        functions_.add(new Min());
        functions_.add(new Pow());
        functions_.add(new Rand());
//        functions_.add(new Rint());
//        functions_.add(new Round());
        functions_.add(new Sin());
        functions_.add(new Sqrt());
        functions_.add(new Tan());
//        functions_.add(new ToDegrees());
//        functions_.add(new ToRadians());
    }
    
    public HashMap<String, Function> getFunctionMap() {
		if (functionsMap_ == null) {
			functionsMap_ = new HashMap<>();

			for (Function o : functions_) {
				functionsMap_.put(o.name(), o);
			}

		}
		return functionsMap_;
	}

	public Function get(String key) {
		return getFunctionMap().get(key);
	}

	public ArrayList<Function> getFunctions() {
		return functions_;
	}
}