package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static hitlist.logic.parser.CliSyntax.PREFIX_EMAIL;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CliSyntax.PREFIX_PHONE;
import static hitlist.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import hitlist.commons.core.index.Index;
import hitlist.commons.util.CollectionUtil;
import hitlist.commons.util.ToStringBuilder;
import hitlist.logic.Messages;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.person.Address;
import hitlist.model.person.Email;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.model.person.Phone;

/**
 * Edits the details of an existing person in the HitList.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + " NAME] "
            + "[" + PREFIX_PHONE + " PHONE] "
            + "[" + PREFIX_EMAIL + " EMAIL] "
            + "[" + PREFIX_ADDRESS + " ADDRESS] "
            + "Note: Use " + PREFIX_EMAIL + " or " + PREFIX_ADDRESS + " without a value to clear the field.\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + " 91234567 "
            + PREFIX_EMAIL + " johndoe@example.com\n"
            + "Example to clear email: " + COMMAND_WORD + " 1 " + PREFIX_EMAIL;

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the hitlist.";
    public static final String MESSAGE_DUPLICATE_CONTACT = "This number is already registered in the hitlist to another person.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        boolean isDuplicateName = model.getHitList().getPersonList().stream()
                .anyMatch(person -> !person.equals(personToEdit) && person.getName().equals(editedPerson.getName()));

        if (isDuplicateName) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        boolean isDuplicatePhone = model.getHitList().getPersonList().stream()
                .anyMatch(person -> !person.equals(personToEdit) && person.getPhone().equals(editedPerson.getPhone()));

        if (isDuplicatePhone) {
            throw new CommandException(MESSAGE_DUPLICATE_CONTACT);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());

        // Handle email: if clearEmail flag is true, set to empty; otherwise use provided value or keep existing
        Optional<Email> updatedEmail;
        if (editPersonDescriptor.isClearEmail()) {
            updatedEmail = Optional.empty();
        } else if (editPersonDescriptor.getEmail().isPresent()) {
            updatedEmail = editPersonDescriptor.getEmail();
        } else {
            updatedEmail = personToEdit.getEmail();
        }

        // Handle address: if clearAddress flag is true, set to empty; otherwise use provided value or keep existing
        Optional<Address> updatedAddress;
        if (editPersonDescriptor.isClearAddress()) {
            updatedAddress = Optional.empty();
        } else if (editPersonDescriptor.getAddress().isPresent()) {
            updatedAddress = editPersonDescriptor.getAddress();
        } else {
            updatedAddress = personToEdit.getAddress();
        }

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private boolean clearEmail = false;
        private boolean clearAddress = false;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setClearEmail(toCopy.clearEmail);
            setClearAddress(toCopy.clearAddress);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address) || clearEmail || clearAddress;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setClearEmail(boolean clearEmail) {
            this.clearEmail = clearEmail;
        }

        public boolean isClearEmail() {
            return clearEmail;
        }

        public void setClearAddress(boolean clearAddress) {
            this.clearAddress = clearAddress;
        }

        public boolean isClearAddress() {
            return clearAddress;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && clearEmail == otherEditPersonDescriptor.clearEmail
                    && clearAddress == otherEditPersonDescriptor.clearAddress;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("clearEmail", clearEmail)
                    .add("clearAddress", clearAddress)
                    .toString();
        }
    }
}
