package ghost;

public enum Move{
    /**
     * Denotes stationary
     */
    Stop,

    /**
     * Denotes right
     */
    R,

    /**
     * Denotes left
     */
    L,

    /**
     * Denotes up
     */
    U,

    /**
     * Denotes down
     */
    D;

    /**
     * @return, The opposite direction
     */
    public Move opposite(){
        if(this == R){
            return L;
        } else if(this == L){
            return R;
        } else if(this == U){
            return D;
        } else if(this == D){
            return U;
        }
        return Stop;
    }
}