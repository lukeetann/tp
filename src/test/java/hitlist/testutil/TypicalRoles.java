package hitlist.testutil;

import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_PRODUCT_MANAGER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_SOFTWARE_ENGINEER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hitlist.model.HitList;
import hitlist.model.company.role.Role;

/**
 * A utility class containing a list of {@code Role} objects to be used in tests.
 */
public class TypicalRoles {

    public static final Role LECTURER = new RoleBuilder()
            .withName("Lecturer")
            .withDescription("An academic role responsible for teaching and conducting research "
                    + "in a university or college.")
            .build();

    public static final Role DATA_SCIENTIST = new RoleBuilder()
            .withName("Data Scientist")
            .withDescription("A data science role responsible for analyzing and interpreting complex data to help "
                    + "organizations make informed decisions.")
            .build();

    // Manually added - Role's details found in {@code CommandTestUtil}
    public static final Role PRODUCT_MANAGER = new RoleBuilder()
            .withName(VALID_ROLE_NAME_PRODUCT_MANAGER)
            .withDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
            .build();

    public static final Role SOFTWARE_ENGINEER = new RoleBuilder()
            .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
            .withDescription(VALID_ROLE_DESCRIPTION_SOFTWARE_ENGINEER)
            .build();

    private TypicalRoles() {} // prevents instantiation

    /**
     * Returns an {@code HitList} with all the typical roles.
     */
    public static HitList getTypicalHitList() {
        HitList hl = new HitList();
        for (Role role : getTypicalRoles()) {
            hl.addRole(role);
        }
        return hl;
    }

    public static List<Role> getTypicalRoles() {
        return new ArrayList<>(Arrays.asList(LECTURER, DATA_SCIENTIST, PRODUCT_MANAGER, SOFTWARE_ENGINEER));
    }
}
