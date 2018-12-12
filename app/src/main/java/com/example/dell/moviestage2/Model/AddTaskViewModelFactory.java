package com.example.dell.moviestage2.Model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dell.moviestage2.Database.AppDatabase;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase database;
    private int id;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTaskViewModel(database, id);

    }

    public AddTaskViewModelFactory(AppDatabase database, int id) {
        this.id = id;
        this.database = database;
    }
}
