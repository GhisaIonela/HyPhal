package com.example.networkgui.customWidgets;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.util.stream.Collectors;

public class ListResponder {

    public static void main(String[] args) {
        final ObservableList<Fruit> fruit = FXCollections.observableArrayList(
                new Callback<Fruit, Observable[]>() {
                    @Override
                    public Observable[] call(Fruit param) {
                        return new Observable[]{
                                param.nameProperty(),
                                param.descriptionProperty()
                        };
                    }
                }
        );

        fruit.addListener(new ListChangeListener<Fruit>() {
            @Override
            public void onChanged(Change<? extends Fruit> c) {
                while (c.next()) {
                    if (c.wasPermutated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            System.out.println("Permuted: " + i + " " + fruit.get(i));
                        }
                    } else if (c.wasUpdated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            System.out.println("Updated: " + i + " " + fruit.get(i));
                        }
                    } else {
                        for (Fruit removedItem : c.getRemoved()) {
                            System.out.println("Removed: " + removedItem);
                        }
                        for (Fruit addedItem : c.getAddedSubList()) {
                            System.out.println("Added: " + addedItem);
                        }
                    }
                }
            }
        });

        fruit.addAll(
                new Fruit("Apple", "Green Skin"),
                new Fruit("Pear", "Yellow Skin")
        );

        fruit.get(0).setDescription("Red Skin");

        fruit.add(
                new Fruit("Peach", "Giant")
        );

        fruit.remove(1);

        System.out.println(
                fruit.stream()
                        .map(Fruit::toString)
                        .collect(Collectors.joining("\n"))
        );
    }
}

class Fruit {
    final StringProperty name;
    final StringProperty description;

    public Fruit(String name, String description) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Fruit{");
        sb.append("name=").append(name.get());
        sb.append(", description=").append(description.get());
        sb.append('}');
        return sb.toString();
    }
}