package com.example.growthon_todolist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import com.example.growthon_todolist.R;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout todoListContainer;
    private int totalTasks = 0;
    private int completedTasks = 0;
    private ArrayList<String> todoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        todoListContainer = findViewById(R.id.todo_list_container);
        ImageButton addTaskButton = findViewById(R.id.add_task_button);
        ImageButton backButton = findViewById(R.id.back_button);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 뒤로 가기 기능 구현
            }
        });

        updateProgressBar(); // 초기 진행률 업데이트
    }

    private void addNewTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_todo, null);
        builder.setView(dialogView);

        EditText editTodoText = dialogView.findViewById(R.id.edit_todo_text);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = editTodoText.getText().toString();
                if (!newTask.isEmpty()) {
                    todoList.add(newTask);
                    totalTasks++;
                    addTaskToLayout(newTask, todoList.size() - 1);
                    updateProgressBar();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addTaskToLayout(String task, int position) {
        // 새로운 할 일 항목을 추가하는 레이아웃을 생성
        LinearLayout newTaskLayout = new LinearLayout(this);
        newTaskLayout.setOrientation(LinearLayout.HORIZONTAL);
        newTaskLayout.setPadding(8, 8, 8, 8);
        newTaskLayout.setBackground(getResources().getDrawable(R.drawable.todo_item_background));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 8); // 하단 여백 추가
        newTaskLayout.setLayoutParams(layoutParams);

        // 새로운 체크박스를 생성하고 레이아웃에 추가
        CheckBox newTaskCheckBox = new CheckBox(this);
        newTaskCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newTaskCheckBox.setText(task);
        newTaskCheckBox.setTextColor(getResources().getColor(R.color.text_primary));

        // 체크박스 상태 변화 리스너 설정
        newTaskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    completedTasks++;
                } else {
                    completedTasks--;
                }
                updateProgressBar();
            }
        });

        // 수정 버튼 생성 및 추가
        ImageButton editButton = new ImageButton(this);
        editButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        editButton.setImageResource(android.R.drawable.ic_menu_edit);
        editButton.setBackground(null); // 배경 제거
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(position);
            }
        });

        newTaskLayout.addView(newTaskCheckBox);
        newTaskLayout.addView(editButton);
        todoListContainer.addView(newTaskLayout);
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_todo, null);
        builder.setView(dialogView);

        EditText editTodoText = dialogView.findViewById(R.id.edit_todo_text);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        editTodoText.setText(todoList.get(position));

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTask = editTodoText.getText().toString();
                if (!updatedTask.isEmpty()) {
                    todoList.set(position, updatedTask);
                    updateTodoList();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateTodoList() {
        todoListContainer.removeAllViews();
        for (int i = 0; i < todoList.size(); i++) {
            addTaskToLayout(todoList.get(i), i);
        }
    }

    private void updateProgressBar() {
        if (totalTasks == 0) {
            progressBar.setProgress(0);
        } else {
            int progress = (completedTasks * 100) / totalTasks;
            progressBar.setProgress(progress);
        }
    }
}
