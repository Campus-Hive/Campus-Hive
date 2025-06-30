<H1 align="center">
CampusHive 🎓
</H1>

<p align="center">
 <img src="images/CampusHive_Logo.png" alt="CampusHive Logo" width="400"/>
</p>

Campus Hive is a JavaFX-based application designed to foster an interactive community for instructors, students, and admins enrolled in the CSE 360 course. It offers a platform for users to create posts, ask questions, and engage in discussions about course content, including GitHub, Java, JavaFX, Eclipse, and more. The application promotes collaborative learning and problem-solving within the course framework.

## Features

### 1. **Post Creation**
- Instructors, students, and admins can create posts on topics ranging from course-related challenges to technical issues.

### 2. **Q&A Forum**
- Users can post questions about course-related difficulties, whether it's about coding issues, development tools, or project setup.
- Replies from peers and instructors help foster community-based learning.

### 3. **Role-Based Interface**
- **Instructors:** Post announcements, provide guidance, and moderate discussions.
- **Students:** Ask questions, contribute to discussions, and receive feedback from both peers and instructors.
- **Admins:** Manage user accounts, moderate content, and oversee platform usage.

### 4. **Topics Covered**
- **GitHub:** Version control, repository management, and collaboration.
- **Java:** Programming concepts, debugging, and best practices.
- **JavaFX:** GUI development and implementation strategies.
- **Eclipse:** IDE setup, configuration, and troubleshooting.

### 5. **Community Support**
- Posts are visible to the entire community, encouraging collaborative problem-solving.
- Users can reply to questions, provide solutions, or offer guidance.

---

## Requirements

To run **Campus Hive**, ensure you have the following installed:

1. **Java Development Kit (JDK) 21.0.4**
   - Make sure that the Java version used is **JDK 21.0.4** or later. You can download it from [Oracle's official website](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **JavaFX SDK 21.0.4**
   - Download and configure the **JavaFX SDK** from [OpenJFX](https://openjfx.io). You will need to include JavaFX libraries in your project setup.

3. **IDE Setup (Eclipse or IntelliJ)**
   - The project can be cloned and opened in any modern Java IDE like **Eclipse** or **IntelliJ IDEA**.
   - Ensure the IDE is configured with the **JavaFX** libraries. See detailed setup instructions below.

4. **H2 Database**
   - The application uses an H2 database to store user data. Ensure you have the **H2 database driver** included in your project setup.

5. **Git (Optional)**
   - **Git** is optional but recommended for managing your project repository.

---

## Referenced Libraries

Below are the libraries required to run **Campus Hive**:

- **JavaFX 21.0.4**:
  - `javafx.base.jar`
  - `javafx.controls.jar`
  - `javafx.fxml.jar`
  - `javafx.graphics.jar`
  - `javafx.media.jar`
  - `javafx.swing.jar`
  - `javafx.web.jar`
  - `javafx-swt.jar`

- **H2 Database Engine**:
  - `h2-2.2.232.jar`

- **Java Activation Framework (javax.activation)**:
  - `javax.activation.jar`

- **JavaMail API**:
  - `javax.mail.jar`

---

## Installation & Usage

### 1. Clone the Repository

To get a local copy of the project, clone it using Git:

```bash
git clone https://github.com/Abhinav-ranish/Campus-Hive.git
```

Alternatively, you can download the ZIP file from the GitHub repository and extract it.

### 2. Set Up JavaFX

Once you've cloned the project:
- Download the **JavaFX SDK** from [OpenJFX](https://openjfx.io/).
- Add the JavaFX SDK library files to your project. For example, in Eclipse:
  - Right-click the project > Build Path > Add Libraries > JavaFX SDK.
- Ensure you add the required JavaFX modules to your VM arguments:

```bash
--module-path /path-to-javafx-sdk/lib --add-modules=javafx.controls,javafx.fxml
```

### 3. Configure H2 Database

The **Campus Hive** project uses H2 for data persistence:
- Ensure the H2 database driver is included in the project's classpath.
- The driver is already included in the project as `h2-2.2.232.jar`.

### 4. Run the Application
- After setting up the JavaFX SDK and H2 database, navigate to the main application file in the **src/application** directory.
- Run the application from your IDE.

### 5. Configure Environment Variables
- Create a `.env` file (see `.env.example`) and define `ENCRYPTION_KEY` with the key used for article encryption.

---

## Project Structure
```
├── bin/
├── data/
├── images/
├── lib/
├── resources/
├── src/
├── README.md
├── build.fxbuild
```

---

## Automated Testing with JUnit

To perform automated testing using JUnit with the test files located in your `src` folder, follow these instructions based on your development environment:

### Eclipse

1. **Set Up JUnit:**
   - Ensure JUnit is included in your project. You can add it by right-clicking on your project, selecting "Build Path" > "Add Libraries," and then choosing "JUnit."

2. **Run Tests:**
   - Navigate to the `src` folder where your test files are located.
   - Right-click on the test file or the entire test package.
   - Select "Run As" > "JUnit Test" to execute the tests.

### IntelliJ IDEA

1. **Set Up JUnit:**
   - IntelliJ IDEA typically includes JUnit by default. If not, you can add it via "File" > "Project Structure" > "Libraries."

2. **Run Tests:**
   - Open the `src` folder and locate your test files.
   - Right-click on a test file or directory containing tests.
   - Choose "Run 'TestName'" to execute the tests.

### Other IDEs or Command Line

1. **Set Up JUnit:**
   - Ensure JUnit is added to your project's dependencies. This might involve configuring a build tool like Maven or Gradle.

2. **Run Tests:**
   - Use the IDE's specific feature to run JUnit tests, often found under a "Run" menu.
   - Alternatively, use command line tools:
     - For Maven: Run `mvn test`.
     - For Gradle: Run `gradle test`.

By following these steps, you can easily automate the testing of CampusHive using JUnit across different development environments.

---

## Credits

**Campus Hive** uses the following third-party libraries and software:
- **JavaFX SDK** for creating the graphical user interface (GUI).
- **H2 Database Engine** for managing and accessing the embedded H2 database.
- **JavaMail API** for handling email functionalities.
- **Java Activation Framework** for handling MIME data types in Java applications.

Additionally, Campus Hive includes a custom **in-house encryption system** developed by the project team for enhanced data security.

---

## Copyright

**Campus Hive** © 2024. All rights reserved.

This project is licensed under proprietary rights by the original developers. You may use the application for personal or educational purposes, but commercial use is prohibited without explicit permission from the authors.

The project uses the following third-party libraries and frameworks:
- **JavaFX SDK**: Licensed under the [GNU General Public License (GPL)](https://openjdk.java.net/legal/gplv2+ce.html), version 2, with the Classpath Exception.
- **H2 Database Engine**: Licensed under the [Mozilla Public License 2.0](https://www.h2database.com/html/license.html) or the [Eclipse Public License 1.0](https://www.h2database.com/html/license.html).
- **JavaMail API**: Distributed under the [GPL v2 + CPE](https://javaee.github.io/javamail/).
- **Java Activation Framework**: Licensed under the [Common Development and Distribution License (CDDL)](https://opensource.org/licenses/CDDL-1.0).

Please consult the respective licenses for each library for more information. Use of these libraries is subject to their respective terms and conditions.

Unauthorized use, reproduction, or distribution of this software, in part or whole, is strictly prohibited. For commercial inquiries or further licensing options, please contact the original developers.

All product names, logos, and brands are the property of their respective owners.
