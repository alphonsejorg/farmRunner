package alphonse.util.farming;

public enum FarmRun {
    HERB_RUN, HOPS_RUN, FRUIT_TREE_RUN, TREE_RUN, NIL;

    @Override
    public String toString() {
        return super.toString().toLowerCase().replaceAll("_", " ");
    }
}