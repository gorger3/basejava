package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            writeWithException(r.getContacts().entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            Map<SectionType, Section> sections = r.getSections();
            writeWithException(sections.entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().toString());
                switch (entry.getKey()) {
                    case PERSONAL:
                    case OBJECTIVE:
                        String content = ((TextSection) entry.getValue()).getContent();
                        dos.writeUTF(content);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = ((ListSection) entry.getValue()).getItems();
                        writeWithException(items, dos, dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = ((OrganizationSection) entry.getValue()).getOrganizations();
                        writeWithException(organizations, dos, organization -> {
                            dos.writeUTF(organization.getHomePage().getName());
                            dos.writeUTF(organization.getHomePage().getUrl() != null ? organization.getHomePage().getUrl() : "");
                            List<Organization.Position> positions = organization.getPositions();
                            writeWithException(positions, dos, position -> {
                                dos.writeUTF(position.getStartDate().toString());
                                dos.writeUTF(position.getEndDate().toString());
                                dos.writeUTF(position.getTitle());
                                dos.writeUTF(position.getDescription() != null ? position.getDescription() : "");

                            });
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            // чтение контактов
            readWithException(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            // чтение секций
            readWithException(dis, () -> { //лямбда для чтения секции
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        Section testSection = new TextSection(dis.readUTF());
                        resume.addSection(sectionType, testSection);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = new ArrayList<>();
                        readWithException(dis, () -> fillTheList(items, list -> list.add(dis.readUTF())));
                        resume.addSection(sectionType, new ListSection(items));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = new ArrayList<>();
                        readWithException(dis, () -> fillTheList(organizations, list -> {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            if (url.equals("")) {
                                url = null;
                            }
                            Link homePage = new Link(name, url);
                            List<Organization.Position> positions = new ArrayList<>();
                            readWithException(dis, () -> fillTheList(positions, list1 -> {
                                LocalDate startDate = LocalDate.parse(dis.readUTF());
                                LocalDate endDate = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = dis.readUTF();
                                if (description.equals("")) {
                                    description = null;
                                }
                                Organization.Position position = new Organization.Position(
                                        startDate,
                                        endDate,
                                        title,
                                        description);
                                positions.add(position);
                            }));
                            organizations.add(new Organization(homePage, positions));
                        }));
                        resume.addSection(sectionType, new OrganizationSection(organizations));
                        break;
                }
            });
            return resume;
        }
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, DataConsumer<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.writeData(t);
        }
    }

    @FunctionalInterface
    private interface DataConsumer<T> {
        void writeData(T t) throws IOException;
    }

    private void readWithException(DataInputStream dis, DataSupplier action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.readData();
        }
    }

    @FunctionalInterface
    private interface DataSupplier {
        void readData() throws IOException;
    }

    private <T> List<T> fillTheList(List<T> list, AddItemToList<T> adder) throws IOException {
        adder.addToList(list);
        return list;
    }

    @FunctionalInterface
    private interface AddItemToList<T> {
        void addToList(List<T> list) throws IOException;
    }
}
