# Standard Export Library for Java

This is the home page of the Mac Export Library for Java(or JVM platform in general). This library convert any POJO to specific export format.

## Description
This library converts list of Java Objects to specific export files. Conversion just need to specify in POJO class. Library automatically use that POJO to convert to downloadable files.

## Supported Formats
* CSV
* Excel
* PDF

## Supported Naming Convention
* Snake Case
* Capatilize Case

### Maven Dependency

```
<dependency>
  <groupId>com.mac.export</groupId>
	<artifactId>export-library</artifactId>
	<version>0.0.1</version>
</dependency>
```

### Executing program

* CSV
```
List<POJO> pojos = new ArrayList<POJO>();
ExportWriter<POJO> export = new CsvExportWriter<POJO>(fileName);
export.write(pojos);
export.close;
export.getFileName();//Provides full path with filename.
```
* Excel
```
List<POJO> pojos = new ArrayList<POJO>();
ExportWriter<POJO> export = new ExcelExportWriter<POJO>(fileName);
export.write(pojos);
export.close;
export.getFileName();//Provides full path with filename.
```
* PDF
```
List<POJO> pojos = new ArrayList<POJO>();
ExportWriter<POJO> export = new PdfExportWriter<POJO>(fileName);
export.write(pojos);
export.close;
export.getFileName();//Provides full path with filename.
```
### POJO Class

```
@HeaderNaming(value = CapitalizeCaseStrategy.class)
public class OnlineOrderDTO {
  private String firstName;             //First Name
  private String lastName;              //Last Name
  @HeaderName(value = "Created Date")
  private Date createdAt;               //Created Date
}
```

## Authors

Contributors names and contact info

[@Mahak Jain](https://github.com/mac1204)

## Version History

* 0.0.1
    * Initial Release

## License

This project is licensed under the MIT License - see the LICENSE.md file for details
