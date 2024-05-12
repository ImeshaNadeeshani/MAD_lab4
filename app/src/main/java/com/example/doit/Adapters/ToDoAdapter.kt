import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.doit.AddNewTask
import com.example.doit.MainActivity
import com.example.doit.Model.ToDoModel
import com.example.doit.R
import com.example.doit.Utils.DatabaseHandler

class ToDoAdapter(private val db: DatabaseHandler, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private var todoList: MutableList<ToDoModel> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task: CheckBox = itemView.findViewById(R.id.todoCheckBox)

        init {
            task.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (isChecked) {
                    db.updateStatus(todoList[position].id, 1)
                } else {
                    db.updateStatus(todoList[position].id, 0)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = todoList[position]
        holder.task.text = item.task
        holder.task.isChecked = item.status == 1
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setTasks(tasks: List<ToDoModel>) {
        todoList.clear()
        todoList.addAll(tasks)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item = todoList.removeAt(position)
        db.deleteTask(item.id)
        notifyItemRemoved(position)
    }

    fun removeTask(id: Int) {
        val position = todoList.indexOfFirst { it.id == id }
        if (position != -1) {
            todoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun editItem(position: Int, newTaskText: String) {
        val item = todoList[position]
        item.task = newTaskText
        db.updateTask(item.id, newTaskText)
        notifyItemChanged(position)
    }
}
