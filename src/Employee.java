// Mike Hayes
// G#01165321

public class Employee
{
    String fname, minit, lname, SSN, bdate, address, sex, superssn;
    int dNo;
    double salary;

    public Employee()
    {
    }

    public Employee(String fname, String lname, String SSN, int dNo)
    {
        this.fname = fname;
        this.lname = lname;
        this.SSN = SSN;
        this.dNo = dNo;
    }

    public String getFname()
    {
        return fname;
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public String getMinit()
    {
        return minit;
    }

    public void setMinit(String minit)
    {
        this.minit = minit;
    }

    public String getLname()
    {
        return lname;
    }

    public void setLname(String lname)
    {
        this.lname = lname;
    }

    public String getSSN()
    {
        return SSN;
    }

    public void setSSN(String SSN)
    {
        this.SSN = SSN;
    }

    public int getdNo()
    {
        return dNo;
    }

    public void setdNo(int dNo)
    {
        this.dNo = dNo;
    }

    public String getBdate()
    {
        return bdate;
    }

    public void setBdate(String bdate)
    {
        this.bdate = bdate;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getSuperssn()
    {
        return superssn;
    }

    public void setSuperssn(String superssn)
    {
        this.superssn = superssn;
    }

    public double getSalary()
    {
        return salary;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }
}
