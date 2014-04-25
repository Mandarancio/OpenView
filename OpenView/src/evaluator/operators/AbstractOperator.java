package evaluator.operators;

public abstract class AbstractOperator implements Operator
{
    protected String name        = "";
    protected String description = "";
    protected int    input       = 2;
    protected int    priority    = 1;

    public AbstractOperator(String name, int priority)
    {
        this(name, "", priority, 2);
    }

    public AbstractOperator(String name, String description, int priority, int input)
    {
        this.name = name;
        this.priority = priority;
        this.input = input;
        this.description = description;
    }

    public AbstractOperator(String name, int priority, int input)
    {
        this(name, "", priority, input);
    }

    public AbstractOperator(String name, String description, int priority)
    {
        this(name, description, priority, 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.synServer.evaluator.operators.Operator#name()
     */
    @Override
    public String name()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.synServer.evaluator.operators.Operator#priority()
     */
    @Override
    public int priority()
    {
        return priority;
    }

    @Override
    public AbstractOperator clone()
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators.Operator#input()
     */
    @Override
    public int input()
    {
        return input;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators.Operator#description()
     */
    @Override
    public String description()
    {
        return description;
    }

    @Override
    public String symbol()
    {
        return name;
    }

}
