package sheet10;

import java.util.*;

class Person implements Comparable<Person> {
    public final String name; // no privacy

    public Person (String name)  {
        this.name = name;
    }

    public int compareTo (Person other) {
        return name.compareTo(other.name);
    }
}

class Student extends Person {
    public final String matrikelNr;

    public Student (String name, String matrikelNr) {
        super (name);
        this.matrikelNr = matrikelNr;
    }
}

class VeryGoodStudent extends Student {
    public VeryGoodStudent(String name, String matrikelNr) {
        super(name, matrikelNr);
    }
}

class PersonComparator implements Comparator<Person> {
    @Override
    public int compare(Person a, Person b) {
        return a.name.compareTo(b.name);
    }
}

public class Varianz {
    public static void sillyFillWithStudents(List<Student> lst) {
        lst.add(new Student("Igor", "127936123"));
        lst.add(new Student("Heiko", "123897128937"));
        lst.add(new Student("Geraldine", "79878978"));
    }

    public static void fillWithStudents(List<? super Student> lst) {
        lst.add(new Student("Igor", "127936123"));
        lst.add(new Student("Heiko", "123897128937"));
        lst.add(new Student("Geraldine", "79878978"));
    }

    // public static Student getFirstStudent(List<? super Student> lst) {
    //     // Does not compile:
    //     return lst.get(0);
    // }

    public static Student sillyGetFirstStudent(List<Student> lst) {
        return lst.get(0);
    }

    public static Student getFirstStudent(List<? extends Student> lst) {
        return lst.get(0);
    }


    public static void sillySort(List<Student> lst, Comparator<Student> cmp) {
        // ...
    }

    // See http://docs.oracle.com/javase/6/docs/api/java/util/Collections.html#sort(java.util.List, java.util.Comparator)
    public static void sort(List<Student> lst, Comparator<? super Student> cmp) {
        // ...
    }

    public static void main(String[] args) {
        LinkedList<Person> persons = new LinkedList<Person>();
        persons.add(new Person("Harald"));
        persons.add(new Person("Waltraud"));

        LinkedList<Student> students = new LinkedList<Student>();
        students.add(new Student("Jaqueline", "123456"));
        students.add(new Student("Isegrim", "789012"));

        LinkedList<VeryGoodStudent> vgStudents = new LinkedList<VeryGoodStudent>();
        vgStudents.add(new VeryGoodStudent("AH", "007"));

        // Does not compile:
        // LinkedList<Person> morePersons = students;
        LinkedList<? extends Person> morePersons = students;

        Person p = morePersons.get(0);
        // Does not compile:
        // Student s = morePersons.get(0); 
        // morePersons.add(new Student("Ferdinand","123"));

        // Does not compile:
        // sillyFillWithStudents(persons);
        fillWithStudents(persons);

        // Does not compile:
        // fillWithStudents(vgStudents);
        fillWithStudents(students);

        // Does not compile:
        // sillySort(students, new PersonComparator());
        sort(students, new PersonComparator());

        sillyGetFirstStudent(students);
        // Does not compile:
        // sillyGetFirstStudent(vgStudents);
        getFirstStudent(students);
        getFirstStudent(vgStudents);

        List<? super Student> superStudents;
        superStudents = new LinkedList<Object>();
        // Does not compile:
        // superStudents = new LinkedList<VeryGoodStudent>();
        superStudents.add(new VeryGoodStudent("Einsteinchen", "1"));
    }
}
