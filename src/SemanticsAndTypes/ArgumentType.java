package SemanticsAndTypes;

public class ArgumentType {
    public String name;
    public String type;
    public int offset;

    public ArgumentType(String name, String type) {
        this.name = name;
        this.type = type;
        this.offset = -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArgumentType) {
            ArgumentType a = (ArgumentType) obj;
            return a.name.equals(this.name);
        }
        return false;
    }

    public void setVariableOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "under construction";
    }
}
