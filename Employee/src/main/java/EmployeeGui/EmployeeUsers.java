package EmployeeGui;

import javax.xml.bind.annotation.XmlRootElement;

import UtilsImplementations.PersistenceData;

@PersistenceData(relativePath = "/src/main/resources/EmployeeLoginScreen/EmployeeUsers.xml")
@XmlRootElement(name = "EmployeeUsers")
public class EmployeeUsers {
	public String[] users = new String[0];
}
