[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mygreen/super-csv-annotation/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mygreen/super-csv-annotation/)
 [![Javadocs](http://javadoc.io/badge/com.github.mygreen/super-csv-annotation.svg?color=blue)](http://javadoc.io/doc/com.github.mygreen/super-csv-annotation) [![Build Status](https://travis-ci.org/mygreen/super-csv-annotation.svg?branch=master)](https://travis-ci.org/mygreen/super-csv-annotation) [![SonarQube](https://sonarcloud.io/api/project_badges/measure?project=com.github.mygreen%3Asuper-csv-annotation&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.mygreen%3Asuper-csv-annotation)

Super CSV Annotation
====================

This library 'Super CSV' extension library with annotation function.
+ this library automatic building for CellProcessor from Annotation with JavaBean.
+ and simply showing localized messages.

# Depends
------------------------------
+ Java1.8
    - (SuperCSV2.x is Java1.6+, but this library require Java1.8)
+ SuperCSV 2.4+

# Setup

1. Add dependency for Super Csv Annotation
    ```xml
    <dependency>
        <groupId>com.github.mygreen</groupId>
        <artifactId>super-csv-annotation</artifactId>
        <version>2.2</version>
    </dependency>
    ```
2. Add dependency for Logging library. Example Log4j.
    ```xml
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.1</version>
    </dependency>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.14</version>
    </dependency>
    ```

# Build

1. Setup Java SE 8 (1.8.0_121+)
2. Setup Maven
3. Setup Sphinx (building for manual)
    1. install Python
    2. install sphinx and theme for read the docs, janome
    ```console
    # pip install sphinx
    # pip install sphinx_rtd_theme --upgrade
    # pip install janome
    ```
4. Build with Maven
    1. make jar files.
    ```console
    # mvn clean package
    ```
    2. generate site.
    ```console
    # mvn site -Dgpg.skip=true
    ```

# Document
- Project infomation
  - http://mygreen.github.io/super-csv-annotation/index.html
- Manual
  - http://mygreen.github.io/super-csv-annotation/sphinx/index.html
- Javadoc
  - http://mygreen.github.io/super-csv-annotation/apidocs/index.html
  - http://javadoc.io/doc/com.github.mygreen/super-csv-annotation/
