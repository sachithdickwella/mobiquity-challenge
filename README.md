# Mobiquity Challenge

Mobiquity class library development challenge.

## Getting started

### Setting up the dependency
The first step is to include `mobiquity-lib:1.0.0` into your project, for example, as a Gradle compile dependency:
```groovy
implementation "com.mobiquity:mobiquity-lib:1.0.0"
```

As a Maven compile dependency:
```xml
<dependency>
    <groupId>com.mobiquity</groupId>
    <artifactId>mobiquity-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Build
Since the library is not yet setup with maven central or any other repository, users have to build from the source and 
install the dependency to their local repository and refer from there, 

To download the source;
```shell
git clone https://github.com/sachithdickwella/mobiquity-challenge.git
```

To build;
```shell
mvn package
```

To build and install to local repo;
```shell
mvn install
```

or, to clean, build and install to local repo;
```shell
mvn clean install
```

## Usage

Below code snippet shows how to call the class library method and how get the result from the calculation;
```java
import com.mobiquity.packer.Packer;

class MyClass {
    
    public static void main(String[] args) {
        
        var result = Packer.pack("/home/john/Desktop/input-data");
        System.out.println(result);
    } 
}
```
This input `/home/john/Desktop/input-data` file content must follow the standard of the dataset, or else non-compliant 
data points/items will be ignored silently by the method.

Sample dataset would look like this;
```text
81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
8 : (1,15.3,€34)
75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)
```

> Data extraction from the input file tightly coupled with its format, ergo, following the exact convention defined here 
> is crucial.

Sample output respective to the input would like this;
```text
4
-
2,7
8,9
```

Even though the output looks like multiple output stream printouts, this is actually a single `String` value, and it must 
be a single value always.

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/sachithdickwella/mobiquity-challenge/issues).

## License
```text
Copyright (c) 2021-present Mobiquity Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```