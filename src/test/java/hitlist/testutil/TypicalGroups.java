package hitlist.testutil;

import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

/**
 * A utility class containing a list of {@code Group} objects to be used in tests.
 */
public class TypicalGroups {

    public static final Group STUDENTS = new Group(new GroupName("Students"));
    public static final Group UNEMPLOYED = new Group(new GroupName("Unemployed"));
}
