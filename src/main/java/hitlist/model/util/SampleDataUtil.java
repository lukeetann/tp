package hitlist.model.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import hitlist.model.HitList;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Address;
import hitlist.model.person.Email;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.model.person.Phone;
import hitlist.model.tag.Tag;

/**
 * Contains utility methods for populating {@code HitList} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), Optional.of(new Email("alexyeoh@example.com")),
                    Optional.of(new Address("Blk 30 Geylang Street 29, #06-40")), getTagSet()),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), Optional.of(new Email("berniceyu@example.com")),
                    Optional.of(new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18")),
                    getTagSet()),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    Optional.of(new Email("charlotte@example.com")),
                    Optional.of(new Address("Blk 11 Ang Mo Kio Street 74, #11-04")), getTagSet()),
            new Person(new Name("David Li"), new Phone("91031282"), Optional.of(new Email("lidavid@example.com")),
                    Optional.of(new Address("Blk 436 Serangoon Gardens Street 26, #16-43")), getTagSet()),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), Optional.of(new Email("irfan@example.com")),
                    Optional.of(new Address("Blk 47 Tampines Street 20, #17-35")), getTagSet()),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), Optional.of(new Email("royb@example.com")),
                    Optional.of(new Address("Blk 45 Aljunied Street 85, #11-31")), getTagSet())
        };
    }

    public static Group[] getSampleGroups() {
        Person[] samplePersons = getSamplePersons();

        Group studentsGroup = new Group(new GroupName("Students"));
        Group unemployedGroup = new Group(new GroupName("Unemployed"));

        Group[] groups = new Group[] { studentsGroup, unemployedGroup };

        long seed = 676767L;
        Random random = new Random(seed);

        // groups are currently disjoint sets
        for (Person person : samplePersons) {
            int randomGroupIndex = random.nextInt(groups.length);

            groups[randomGroupIndex].addMember(person);
        }

        return groups;
    }

    public static Company[] getSampleCompanies() {
        return new Company[] {
            new Company(new CompanyName("Google Inc."),
                    new CompanyDescription("A multinational technology company specializing in "
                            + "Internet-related services and products")),
            new Company(new CompanyName("Microsoft Corporation"),
                    new CompanyDescription("An American multinational technology company with headquarters "
                            + "in Redmond, Washington")),
            new Company(new CompanyName("Meta Platforms, Inc."),
                    new CompanyDescription("An American multinational technology conglomerate based in "
                            + "Menlo Park, California")),
            new Company(new CompanyName("Apple Inc."),
                    new CompanyDescription("An American multinational technology company specializing in "
                            + "consumer electronics and computer software")),
            new Company(new CompanyName("Amazon.com, Inc."),
                    new CompanyDescription("An American multinational technology company focusing on "
                            + "e-commerce, cloud computing, and digital streaming"))
        };
    }

    public static ReadOnlyHitList getSampleHitList() {
        HitList sampleHitList = new HitList();

        for (Person samplePerson : getSamplePersons()) {
            sampleHitList.addPerson(samplePerson);
        }

        for (Group sampleGroup : getSampleGroups()) {
            sampleHitList.addGroup(sampleGroup);
        }

        for (Company sampleCompany : getSampleCompanies()) {
            sampleHitList.addCompany(sampleCompany);
        }

        return sampleHitList;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
