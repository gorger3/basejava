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
                        resume.addSection(sectionType, new ListSection(readListWithException(dis, dis::readUTF)));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        resume.addSection(sectionType, new OrganizationSection(readListWithException(dis, () -> { // возвращает
                            // список прочитанных организаций, заносит его в секцию и добавляет её в резюме
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            if (url.equals("")) {
                                url = null;
                            }
                            Link homePage = new Link(name, url);
                            return new Organization(homePage, readListWithException(dis, () -> { // возвращает список
                                // прочитанных позиций, заносит его в новую организацию и возвращает её
                                LocalDate startDate = LocalDate.parse(dis.readUTF());
                                LocalDate endDate = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = dis.readUTF();
                                if (description.equals("")) {
                                    description = null;
                                }
                                return new Organization.Position( // возвращает прочитанную позицию
                                        startDate,
                                        endDate,
                                        title,
                                        description);
                            }));
                        })));
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

    private <T> List<T> readListWithException(DataInputStream dis, ListReader<T> list) throws IOException {
        List<T> readerList = new ArrayList<>(); // пустой список, который предстоит заполнить прочитанными записями
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            readerList.add(list.readList()); // чтение записи и добавление её в качестве элемента списка
        }
        return readerList; // прочитанный список
    }

    @FunctionalInterface
    interface ListReader<T> {
        T readList() throws IOException; // читает запись и возвращает прочитанный объект (позицию, организацию, секцию)
    }
}
