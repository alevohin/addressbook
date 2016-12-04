package com.alevohin.addressbook.domain;

public class PersonEvent {

    public static class CRUDEvent<T>{

        private final T entity;

        CRUDEvent(T entity) {
            this.entity = entity;
        }

        public T getEntity() {
            return entity;
        }
    }

    public static class Created extends CRUDEvent<Person> {
        public Created(Person entity) {
            super(entity);
        }
    }

    public static class Deleted extends CRUDEvent<Person> {
        public Deleted(Person entity) {
            super(entity);
        }
    }

    public static class Updated extends CRUDEvent<Person> {
        public Updated(Person entity) {
            super(entity);
        }
    }
}
