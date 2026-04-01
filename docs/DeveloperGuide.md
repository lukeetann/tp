---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# HitList Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).


--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `CompanyListPanel`,  `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person`, and `Company` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to a `HitListParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `HitListParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `HitListParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores HitList data i.e., all `Person`, `Group` and `Company` objects (which are contained in a `UniquePersonList`, `UniqueGroupList` and `UniqueCompanyList` object).
* stores the currently 'selected' `Person`, `Group` or `Company` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>`, `ObservableList<Group>` or `ObservableList<Company>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both HitList data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `HitListStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `hitlist.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Company Profile

A `Company` object represents a company profile. It has the following details:
* `companyName` (required): The name of the company.
* `companyDescription` (required): A description of the company.
* `companyRoles` (optional): A list of roles that the headhunter is recruiting for within the company.

A `Role` object represents a role that the headhunter is recruiting for within a company. It has the following details:
* `companyRole` (required): The name of the role.
* `companyRoleDescription` (required): A description of the role.

#### Design considerations for Company Parameters:

**Aspect: Company Field Requirements:**
* **Alternative 1 (current choice):** Both company name and description are required fields.
    * Pros: Ensures that all company profiles have a minimum level of information, which can be useful for the headhunter to quickly identify and differentiate between companies.
    * Cons: May be too restrictive for users who want to quickly add a company profile with minimal information and fill in the details later.

* **Alternative 2:** Only the company name is required, while the description is optional.
    * Pros: Provides more flexibility for users to add company profiles with minimal information and update them later as needed.
    * Cons: May lead to incomplete company profiles that lack important information, making it harder for the headhunter to manage their client base effectively.

**Aspect: Validation of Company Names:**
* **Alternative 1:** Use strict regex ^[\p{Alnum}][\p{Alnum} ]*$ to only allow alphanumeric characters and spaces. 
    * Pros: Prevents users from accidentally entering malformed data or using symbols that might break the CLI formatting. 
    * Cons: Prevents users from adding companies with valid symbols in their names (e.g., 'LEAK X'PRESS' PLUMBING & CONSTRUCTION).

* **Alternative 2 (current choice):** Use a custom regex [^[^\s/][^/\v]{1,29}$] (Must not start with a space, contain / or have newlines and be within the length limit of 2 to 30 characters).
    * Pros: Highly flexible for the user. 
    * Cons: Could allow completely nonsensical company names like !!! or ???.

**Aspect: Validation of Company Description:**
* **Alternative 1:** Use strict regex ^[\p{Alnum}][\p{Alnum} ]*$ to only allow alphanumeric characters and spaces.
    * Pros: Prevents users from accidentally entering malformed data or using symbols that might break the CLI formatting.
    * Cons: Prevents users from adding companies with valid symbols in their names (e.g., 'LEAK X'PRESS' PLUMBING & CONSTRUCTION).

* **Alternative 2 (current choice):** Use a custom regex [^[^\s/][^/\v]{1,999}$] (Must not start with a space, contain / or have newlines and be within the length limit of 2 to 1000 characters).
    * Pros: Highly flexible for the user.
    * Cons: Could allow completely nonsensical company names like !!! or ???.

#### Design considerations for Company Commands:

**Aspect: Command Format for Parameters:**

* **Alternative 1 (current choice):** Use prefixes to indicate parameters (e.g., `/c` for company name, `/d` for description).
    * Pros: Clear and unambiguous parsing of parameters, especially when there are multiple parameters.
    * Cons: Requires users to remember and use specific prefixes.

* **Alternative 2:** Use a fixed order of parameters without prefixes (e.g., `cmpadd Google Tech Company`).
    * Pros: Simpler command format, less typing for users.
    * Cons: Parsing can be more error-prone, especially if parameters can contain spaces or if there are optional parameters.

**Aspect: Handling Duplicate Companies:**

* **Alternative 1 (current choice):** Check for duplicates based on company name and reject the addition if a duplicate is found.
    * Pros: Prevents cluttering the HitList with duplicate entries, maintains data integrity.
    * Cons: Does not account for edge cases where two distinct companies might share the same names.

* **Alternative 2:** Allow duplicates but provide a warning to the user.
    * Pros: Provides flexibility for users who may want to add similar companies, avoids false positives in duplicate detection.
    * Cons: Can lead to a cluttered HitList and make it harder for users to manage their contacts effectively.

#### Adding a company

The AddCompany mechanism is facilitated by `AddCompanyCommand` and its associated parser `AddCompanyCommandParser`. It allows users to add a new company to the HitList.
The feature implements the following key operations:

* `AddCompanyCommandParser#parse()` — Parses the user input to extract the company name (indicated by the `/c` prefix) and the description (indicated by the `/d` prefix). 
* `AddCompanyCommand#execute()` — Executes the logic to add the parsed company to the model. 
* `Model#addCompany()` — Updates the HitList within the Model state with the newly created company.

Given below is an example usage scenario and how the AddCompany mechanism behaves at each step.

Step 1. The user launches the application and types `cmpadd /c Google /d Tech Company` into the command box.

Step 2. The `LogicManager` intercepts the user input and calls `HitListParser#parseCommand("cmpadd /c Google /d Tech Company")`.

Step 3. Recognizing the `cmpadd` command word, the `HitListParser` instantiates an `AddCompanyCommandParser`.

Step 4. The `HitListParser` calls the `parse(" /c Google /d Tech Company")` method of the newly created `AddCompanyCommandParser`. The parser extracts the company details, creates a new Company object (representing Google), and passes it into the constructor of a new `AddCompanyCommand`.

<puml src="diagrams/add-company/CompanyAddParsing.puml" alt="CompanyAddObjectDiagram" />

Step 5. The `AddCompanyCommand` is returned to the `LogicManager`, and the `AddCompanyCommandParser` is subsequently destroyed.

<puml src="diagrams/add-company/CompanAddExecution.puml" alt="CompanyAddObjectDiagram" />

Step 6. `LogicManager` calls `AddCompanyCommand#execute()`. This command calls `Model#addCompany(companyToAdd)`, passing the parsed company object to update the internal `HitList` state.

Step 7. Finally, `Storage` saves the updated `HitList` to the hard disk, and the `LogicManager` returns the `CommandResult` to the UI to display a success message to the user.

<puml src="diagrams/add-company/CompanyAddPostExecution.puml" alt="CompanyAddObjectDiagram" />

The following sequence diagram shows how an AddCompany operation goes through the Logic component:

<puml src="diagrams/add-company/CompanyAddSequenceDiagram-Logic.puml" alt="CompanyAddSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `AddCompanyCommand` and `AddCompanyCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

The following activity diagram summarizes what happens when a user executes the `cmpadd` command:

<puml src="diagrams/add-company/CompanyAddActivityDiagram.puml" width="250" />

#### Deleting a company

The DeleteCompany mechanism is facilitated by `DeleteCompanyCommand` and its associated parser `DeleteCompanyCommandParser`. It allows users to remove an existing company from `HitList`, either by specifying its exact name or its displayed index in the UI.
The feature implements the following key operations:

* `DeleteCompanyCommandParser#parse()` — Parses the user input to determine if the deletion target is an index or a company name (indicated by the `/c` prefix).
* `DeleteCompanyCommand#execute()` — Executes the logic to verify the target's existence and remove it from the model.
* `Model#deleteCompany()` — Updates the HitList within the Model state by removing the specified company.

Given below is an example usage scenario and how the DeleteCompany mechanism behaves at each step.

Step 1. The user launches the application and types `cmpdel /c Google` into the command box.

Step 2. The `LogicManager` intercepts the user input and calls `HitListParser#parseCommand("cmpdel /c Google")`.

Step 3. Recognizing the `cmpdel` command word, the `HitListParser` instantiates a `DeleteCompanyCommandParser`.

Step 4. The `HitListParser` calls the `parse(" /c Google")` method of the newly created `DeleteCompanyCommandParser`. The parser extracts the target company name, creates a new `DeleteCompanyCommand` targeting "Google", and returns it. (Note: If the user had typed `cmpdel 1`, the parser would extract the index instead).

<puml src="diagrams/delete-company/CompanyDeleteParsing.puml" alt="CompanyDeleteObjectDiagram" />

Step 5. The `DeleteCompanyCommand` is returned to the `LogicManager`, and the `DeleteCompanyCommandParser` is subsequently destroyed.

<puml src="diagrams/delete-company/CompanyDeleteExecution.puml" alt="CompanyDeleteObjectDiagram" />

Step 6. `LogicManager` calls `DeleteCompanyCommand#execute()`. The command retrieves the target company and calls `Model#deleteCompany(target)` to remove it from the internal HitList state.

Step 7. Finally, `Storage` saves the updated `HitList` to the hard disk, and the `LogicManager` returns the `CommandResult` to the UI to display a success message to the user.

<puml src="diagrams/delete-company/CompanyDeletePostExecution.puml" alt="CompanyDeleteObjectDiagram" />

The following sequence diagram shows how an AddCompany operation goes through the Logic component:

<puml src="diagrams/delete-company/CompanyDeleteSequenceDiagram-Logic.puml" alt="CompanyDeleteSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCompanyCommand` and `DeleteCompanyCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

The following activity diagram summarizes what happens when a user executes the `cmpdel` command:

<puml src="diagrams/delete-company/CompanyDeleteActivityDiagram.puml" width="250" />

#### Design considerations for Roles Parameters:

**Aspect: Company Field Requirements:**
* **Alternative 1 (current choice):** Both role name and role description are required fields.
    * Pros: Ensures that all roles have a minimum level of information, which can be useful for the headhunter to quickly identify the requirements of clients request.
    * Cons: May be too restrictive for users who want to quickly add a role without the description first and fill in the details later.

* **Alternative 2:** Only the company name is required, while the description is optional.
    * Pros: Provides more flexibility for users to add company profiles with minimal information and update them later as needed.
    * Cons: May lead to incomplete company profiles that lack important information, making it harder for the headhunter to manage their client base effectively.

**Aspect: Validation of Role Names:**
* **Alternative 1:** Use strict regex ^[\p{Alnum}][\p{Alnum} ]*$ to only allow alphanumeric characters and spaces.
    * Pros: Prevents users from accidentally entering malformed data or using symbols that might break the CLI formatting.
    * Cons: Prevents users from adding companies with valid symbols in their names (e.g., Front-end Developer).

* **Alternative 2 (current choice):** Use a custom regex [^[^\s/][^/\v]{1,49}$] (Must not start with a space, contain / or have newlines and be within the length limit of 2 to 50 characters).
    * Pros: Highly flexible for the user.
    * Cons: Could allow completely nonsensical company names like !!! or ???.

**Aspect: Validation of Role Description:**
* **Alternative 1:** Use strict regex ^[\p{Alnum}][\p{Alnum} ]*$ to only allow alphanumeric characters and spaces.
    * Pros: Prevents users from accidentally entering malformed data or using symbols that might break the CLI formatting.
    * Cons: Prevents users from adding companies with valid symbols in their names (e.g., Art Direction + Brand Identity).

* **Alternative 2 (current choice):** Use a custom regex [^[^\s/][^/\v]{1,999}$] (Must not start with a space, contain / or have newlines and be within the length limit of 2 to 1000 characters).
    * Pros: Highly flexible for the user.
    * Cons: Could allow completely nonsensical company names like !!! or ???.

#### Design considerations for Roles Commands:

**Aspect: Command Format for Parameters:**

* **Alternative 1 (current choice):** Use prefixes to indicate parameters (e.g., `/c` for role name, `/d` for role description).
    * Pros: Clear and unambiguous parsing of parameters, especially when there are multiple parameters.
    * Cons: Requires users to remember and use specific prefixes.

* **Alternative 2:** Use a fixed order of parameters without prefixes (e.g., `roleadd Software Engineer Develops Software`).
    * Pros: Simpler command format, less typing for users.
    * Cons: Parsing can be more error-prone, especially if parameters can contain spaces or if there are optional parameters.

**Aspect: Handling Duplicate Roles:**

* **Alternative 1 (current choice):** Check for duplicates based on role name and reject the addition if a duplicate is found.
    * Pros: Prevents cluttering the HitList with duplicate entries, maintains data integrity.
    * Cons: Does not account for edge cases where two distinct role might share the same names.

* **Alternative 2:** Allow duplicates but provide a warning to the user.
    * Pros: Provides flexibility for users who may want to add similar roles, avoids false positives in duplicate detection.
    * Cons: Can lead to a cluttered HitList and make it harder for users to manage the company roles effectively.

#### Adding a role to a specified company

The AddRole mechanism is facilitated by `AddCompanyRoleCommand` and its associated parser `AddCompanyRoleCommandParser`. It allows users to add a new role to an existing company in the HitList.
The feature implements the following key operations:

* `AddCompanyRoleCommandParser#parse()` — Parses the user input to extract the target company (indicated by the `/c` prefix), role name (indicated by the `/r` prefix) and role description (indicated by the `/d` prefix).
* `AddCompanyRoleCommand#execute()` — Executes the logic to add the parsed role to the target company in the model.
* `Model#addCompanyRole()` — Updates the HitList within the Model state by adding the new role to the target company.

Given below is an example usage scenario and how the AddRole mechanism behaves at each step.

Step 1. The user launches the application and types `roleadd /c Google /r Software Engineer /d Develops Software` into the command box.

Step 2. The `LogicManager` intercepts the user input and calls `HitListParser#parseCommand("roleadd /c Google /r Software Engineer /d Develops Software")`.

Step 3. Recognizing the `roleadd` command word, the `HitListParser` instantiates an `AddCompanyRoleCommandParser`.

Step 4. The `HitListParser` calls the `parse(" /c Google /r Software Engineer /d Develops Software")` method of the newly created `AddCompanyRoleCommandParser`. The parser extracts the target company name, role details, creates a new Role object (representing Software Engineer), and passes it into the constructor of a new `AddCompanyRoleCommand`.

<puml src="diagrams/add-role/RoleAddParsing.puml" alt="RoleAddObjectDiagram" />

Step 5. The `AddCompanyRoleCommand` is returned to the `LogicManager`, and the `AddCompanyRoleCommandParser` is subsequently destroyed.

<puml src="diagrams/add-role/RoleAddExecution.puml" alt="RoleAddObjectDiagram" />

Step 6. `LogicManager` calls `AddCompanyRoleCommand#execute()`. This command calls `Model#addCompanyRole(targetCompany, roleToAdd)`, passing the target company and the parsed role object to update the internal `HitList` state.

Step 7. Finally, `Storage` saves the updated `HitList` to the hard disk, and the `LogicManager` returns the `CommandResult` to the UI to display a success message to the user.

<puml src="diagrams/add-role/RoleAddPostExecution.puml" alt="RoleAddObjectDiagram" />

The following sequence diagram shows how an AddRole operation goes through the Logic component:

<puml src="diagrams/add-role/RoleAddSequenceDiagram-Logic.puml" alt="RoleAddSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `AddCompanyRoleCommand` and `AddCompanyRoleCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

The following activity diagram summarizes what happens when a user executes the `roleadd` command:

<puml src="diagrams/add-role/RoleAddActivityDiagram.puml" width="250" />

#### Deleting a role from a specified company

The DeleteRole mechanism is facilitated by `DeleteCompanyRoleCommand` and its associated parser `DeleteCompanyRoleCommandParser`. It allows users to remove an existing role from a company in the HitList, either by specifying the role's name or its displayed index in the UI.
The feature implements the following key operations:

* `DeleteCompanyRoleCommandParser#parse()` — Parses the user input to determine if the deletion target is an index or a role name (indicated by the `/r` prefix), as well as the target company (indicated by the `/c` prefix).
* `DeleteCompanyRoleCommand#execute()` — Executes the logic to verify the target's existence and remove it from the target company in the model.
* `Model#deleteCompanyRole()` — Updates the HitList within the Model state by removing the specified role from the target company.

Given below is an example usage scenario and how the DeleteRole mechanism behaves at each step.

Step 1. The user launches the application and types `roledel /c Google /r Software Engineer` into the command box.

Step 2. The `LogicManager` intercepts the user input and calls `HitListParser#parseCommand("roledel /c Google /r Software Engineer")`.

Step 3. Recognizing the `roledel` command word, the `HitListParser` instantiates a `DeleteCompanyRoleCommandParser`.

Step 4. The `HitListParser` calls the `parse(" /c Google /r Software Engineer")` method of the newly created `DeleteCompanyRoleCommandParser`. The parser extracts the target company name, role name, creates a new `DeleteCompanyRoleCommand` targeting the "Software Engineer" role in "Google", and returns it. (Note: If the user had typed `roledel /c Google 1`, the parser would extract the role index instead).

<puml src="diagrams/delete-role/RoleDeleteParsing.puml" alt="RoleDeleteObjectDiagram" />

Step 5. The `DeleteCompanyRoleCommand` is returned to the `LogicManager`, and the `DeleteCompanyRoleCommandParser` is subsequently destroyed.

<puml src="diagrams/delete-role/RoleDeleteExecution.puml" alt="RoleDeleteObjectDiagram" />

Step 6. `LogicManager` calls `DeleteCompanyRoleCommand#execute()`. The command retrieves the target company and role, and calls `Model#deleteCompanyRole(targetCompany, targetRole)` to remove the role from the target company in the internal HitList state.

Step 7. Finally, `Storage` saves the updated `HitList` to the hard disk, and the `LogicManager` returns the `CommandResult` to the UI to display a success message to the user.

<puml src="diagrams/delete-role/RoleDeletePostExecution.puml" alt="RoleDeleteObjectDiagram" />

The following sequence diagram shows how a DeleteRole operation goes through the Logic component:

<puml src="diagrams/delete-role/RoleDeleteSequenceDiagram-Logic.puml" alt="RoleDeleteSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCompanyRoleCommand` and `DeleteCompanyRoleCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

The following activity diagram summarizes what happens when a user executes the `roledel` command:

<puml src="diagrams/delete-role/RoleDeleteActivityDiagram.puml" width="250" />

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedHitList`. It extends `HitList` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedHitList#commit()` — Saves the current HitList state in its history.
* `VersionedHitList#undo()` — Restores the previous HitList state from its history.
* `VersionedHitList#redo()` — Restores a previously undone HitList state from its history.

These operations are exposed in the `Model` interface as `Model#commitHitList()`, `Model#undoHitList()` and `Model#redoHitList()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedHitList` will be initialized with the initial HitList state, and the `currentStatePointer` pointing to that single HitList state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in HitList. The `delete` command calls `Model#commitHitList()`, causing the modified state of HitList after the `delete 5` command executes to be saved in the `hitListStateList`, and the `currentStatePointer` is shifted to the newly inserted HitList state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitHitList()`, causing another modified HitList state to be saved into the `hitListStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitHitList()`, so HitList state will not be saved into the `hitListStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoHitList()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous HitList state, and restores HitList to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial HitList state, then there are no previous HitList states to restore. The `undo` command uses `Model#canUndoHitList()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoHitList()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores HitList to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `hitListStateList.size() - 1`, pointing to the latest HitList state, then there are no undone HitList states to restore. The `redo` command uses `Model#canRedoHitList()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify HitList, such as `list`, will usually not call `Model#commitHitList()`, `Model#undoHitList()` or `Model#redoHitList()`. Thus, the `hitListStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `hitListStateList`, all HitList states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire HitList.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* headhunters and recruiters
* fast typist
* headhunts for multiple companies
* needs to track both candidate and company contacts
* needs to keep track of candidates' status (unemployed/graduating/etc.)

**Value proposition**: alleviate the logistics of matching candidates to clients


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​    | I want to …​                              | So that I can…​                                                                                  |
|----------|------------|-------------------------------------------|--------------------------------------------------------------------------------------------------|
| `* * *`  | Headhunter | add new candidate contacts                | build a database of potential hires for future placements                                        |
| `* * *`  | Headhunter | delete contacts                           | keep my database uncluttered by removing irrelevant candidates contacts                          |
| `* * *`  | Headhunter | list contacts                             | easily browse my talent pool to locate specific individuals                                      |
| `* * *`  | Headhunter | add contact groups                        | keep track of which candidates are headhunted for the companies                                  |
| `* * *`  | Headhunter | delete contact groups                     | remove the group for a role when it is already filled                                            |
| `* * *`  | Headhunter | list contact groups                       | view a complete overview of all the job groups I have added to the system                        |
| `* * *`  | Headhunter | add contacts to contact groups            | build a targeted shortlist of candidates for a specific job opening                              |
| `* * *`  | Headhunter | delete contacts from contact groups       | keep my shortlist accurate by removing candidates who are no longer in the running for that role |
| `* * *`  | Headhunter | list contact group members                | easily evaluate and compare all shortlisted candidates for a specific open position              |
| `* * *`  | Headhunter | add company profile                       | keep track of the companies I am headhunting for                                                 |
| `* * *`  | Headhunter | delete company profile                    | remove the companies that have stopped using my headhunting services                             |
| `* * *`  | Headhunter | list all company profile                  | view all companies I am headhunting for to get a high-level overview of my client base           |
| `* *`    | Headhunter | add company roles to company profile      | maintain comprehensive records of my clients' requirements and contact information               |
| `* *`    | Headhunter | delete company roles from company profile | keep my client records accurate by removing outdated or incorrect information                    |
| `* *`    | Headhunter | list specific company profile             | review all the active job placements that particular client has hired me to fill                 |

*{More to be added}*

### Use cases

For all use cases below, the **System** is the `HitList`, **Actor** is the `user` and **Precondition** is the `app actively runs and runs on Java 17`, unless specified otherwise

**Use case 1: Add a contact**

**MSS**

1.  User requests to add a contact
2.  System creates the contact
3.  System confirms that the contact has been created

    Use case ends.

**Extensions**

* 1a. System detects that a contact with the same phone number already exists.
    * 1a1. System shows previously added contact with the same phone number message

    Use case ends.

**Use case 2: Delete a contact**

**MSS**

1.  User requests to delete a contact
2.  System deletes the contact
3.  System confirms that the contact has been deleted

    Use case ends.

**Extensions**

* 1a. System detects that the requested contact does not exist.
    * 1a1. System shows requested contact does not exist message

    Use case ends.

**Use case 3: List contacts**

**MSS**

1.  User requests to list all contacts
2.  System displays all contacts

    Use case ends.

**Extensions**

* 2a. System detects that the contact list is empty
    * 2a1. System shows contact list is empty message

    Use case ends.

**Use case 4: Add a contact group**

**MSS**

1. User requests to add a contact group
2. System creates the contact group
3. System informs user that the contact group has been created

    Use case ends.

**Extensions**

* 2a. System detects that a company profile with the same name already exists
    * 2a1. System shows company profile already exists message

    Use case ends.

**Use case 5: Delete a contact group**

**MSS**

1. User requests to delete a contact group
2. System deletes the contact group
3. System informs user that the contact group has been deleted

    Use case ends.

**Extensions**

* 1a. System detects that the contact group does not exist.
    * 1a1. System shows contact group does not exist message

    Use case ends.

**Use case 6: List contact groups**

**MSS**

1.  User requests to list all contact groups
2.  System displays all contacts

    Use case ends.

**Extensions**

* 2a. System detects that there are no contact groups
    * 2a1. System shows no contact groups message

    Use case ends.

**Use case 7: Add a contact to a contact group**

**MSS**

1. User <u>creates a contact (UC1)</u>
2. User <u>creates a contact group (UC4)</u>
3. User requests to add the contact to the contact group
4. System creates the contact group
5. System informs user that the contact group has been created

    Use case ends.

**Extensions**

* 4a. System detects that the contact is already in the contact group
    * 4a1. System shows contact is already in the contact group message
* 4b. System detects that contact group does not exist
    * 4b1. System shows contact group does not exist message
* 4c. System detects that contact does not exist
    * 4c1. System shows contact does not exist message

    Use case ends.

**Use case 7: Remove contacts from contact group**

**MSS**

1. User requests remove a contact from a contact group
2. System informs user that the contact has been removed from the contact group

    Use case ends.

**Extensions**

* 2a. System detects there is no such contact in the contact group
    * 2a1. System shows contact is not in the contact group message
* 2b. System detects that contact group does not exist
    * 2b1. System shows contact group does not exist message
* 2c. System detects that contact does not exist
    * 2c1. System shows contact does not exist message

    Use case ends.

**Use case 8: List contact group members**

**MSS**

1.  User requests to list contact group members of a specified contact group
2.  System displays all contact group members of the specified contact group

    Use case ends.

**Extensions**

* 2a. System detects that the specified contact group does not exist
    * 2a1. System shows contact group does not exist message

    Use case ends.

**Use case 9: Add a company profile**

**MSS**

1.  User requests to add a company profile
2.  System creates the company profile
3.  System confirms that the company profile has been created

    Use case ends.

**Extensions**

* 1a. System detects that a company profile with the same name already exists
    * 1a1. System shows company profile already exists message

    Use case ends.

**Use case 10: Delete a company profile**

**MSS**

1.  User requests to delete a company profile
2.  System removes the company profile
3.  System confirms that the company profile has been deleted

    Use case ends.

**Extensions**

* 2a. System detects that the specified company does not exist
    * 2a1. System shows company profile does not exist message

    Use case ends.

**Use case 11: List company profile**

**MSS**

1.  User requests to list company profiles
2.  System displays all contact group members of the specified contact group

    Use case ends.

**Extensions**

* 2a. System detects that specified contact group does not exist
    * 2a1. System shows contact group does not exist message

    Use case ends.

**Use case 12: Add company details to company profile**

**MSS**

1.  User <u>adds a company profile (UC9)</u>
2.  User requests to add company details to the company profile
3.  System confirms that the company details has been updated

    Use case ends.

**Extensions**

* 2a. System detects that the specified company profile does not exist
    * 2a1. System shows company profile does not exist message
* 2b. System detects that the company details already exist in the company profile
    *  2b1. System shows company details already exist message

    Use case ends.

**Use case 13: Delete company details from company profile**

**MSS**

1.  User requests to delete a company detail from a company profile
2.  System removes the company detail from the company profile
3.  System confirms that the company detail has been deleted

    Use case ends.

**Extensions**

* 2a. System detects that the specified company does not exist
    * 2a1. System shows company profile does not exist message
* 2b. System detects that the company detail does not exist in the company profile
    * 2b1. System shows company detail does not exist message

    Use case ends.

**Use case 14: List a specific company profile**

**MSS**

1.  User requests to view a company profile by name
2.  System retrieves the company profile
3.  System displays the company name and all stored details

    Use case ends.

**Extensions**

* 1a. System detects that the specified company does not exist
    * 1a1. System shows company profile does not exist message

    Use case ends.

*{More to be added}*

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system should be able to run without internet access.
5. The system should respond to the user within 2 seconds for all valid user commands.
6. The system should remain responsive while processing invalid user commands and should return an appropriate error message.
7. The system should be able to support case-insensitive unique identifiers for contacts, contact groups and company profiles.

### Contact Non-Functional Requirements

1. The system should be able to support up to 1000 contacts without exceeding the 2 seconds response time limit for operations such as adding, deleting, listing of contacts.

### Contact Group Non-Functional Requirements

1. The system should be able to support at least 500 contact groups without exceeding the 2 seconds response time limit for operations such as adding, deleting, listing of contact groups.
2. The system should be able to support at least 100 contacts in a contact group without exceeding the 2 seconds response time limit for operations such as adding, deleting, listing of contact group members.

### Company Profile Non-Functional Requirements

1. The system should support at least 100 company profiles without exceeding the 2 seconds response time limit for operations such as adding, deleting, listing of company profiles.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Above average typing speed**: 40 words per minute (wpm) or more for regular English text (i.e. not code, not system admin commands)
* **Valid user command**: A user command that is correctly formatted and does not violate any constraints of the system.
* **Invalid user command**: A user command that is incorrectly formatted or violates constraints of the system.
* **Contact**: A stored record representing a potential candidate that the headhunter is recruiting for.
* **Contact Group**: A tag used to identify different contacts and group similar contacts. A contact group can have none to many contacts.
* **Company Profile**: A stored record representing a client company that the headhunter is recruiting for.
* **Company Description**: A detail of a company profile that describes the company. A company profile must have a company description.
* **Company Role**: A detail of a company profile that describes the role that the headhunter is recruiting for. A company profile may or may not have company roles.
* **Company Role Description**: A detail describing the role that the headhunter is recruiting for within the company. A company role must have a company role description.

*{More to be added}*

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases …​ }_
