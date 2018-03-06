package todo.model;

import javax.persistence.*;


@Entity
@Table(name = "todo")
public class TodoModel {

    private String todoname;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(length = 50)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TODO")
//    @SequenceGenerator(name = "SQ_TODO", sequenceName = "SQ_TODO", allocationSize = 1)
    private int todoid;

//    @Column(length = 50)
    private String listname;


    public String getTodoname() {
        return todoname;
    }


    public void setTodoname(String todoname) {
        this.todoname = todoname;
    }


    public int getTodoid() {
        return todoid;
    }


    public void setTodoid(int todoid) {
        this.todoid = todoid;
    }


    public String getListname() {
        return listname;
    }


    public void setListname(String listname) {
        this.listname = listname;
    }
}
