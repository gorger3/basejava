package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, DataConsumer<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.writeData(t);
        }
    }

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
            int sizeOfContacts = dis.readInt();
            for (int i = 0; i < sizeOfContacts; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sizeOfSections = dis.readInt();
            for (int i = 0; i < sizeOfSections; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        Section testSection = new TextSection(dis.readUTF());
                        resume.addSection(sectionType, testSection);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int sizeOfListSection = dis.readInt();
                        List<String> items = new ArrayList<>();
                        for (int j = 0; j < sizeOfListSection; j++) {
                            items.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new ListSection(items));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        int sizeOfOrganizations = dis.readInt();
                        List<Organization> organizations = new ArrayList<>();
                        for (int j = 0; j < sizeOfOrganizations; j++) {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            if (url.equals("")) {
                                url =  null;
                            }
                            Link homePage = new Link(name, url);
                            int sizeOfPositions = dis.readInt();
                            List<Organization.Position> positions = new ArrayList<>();
                            for (int k = 0; k < sizeOfPositions; k++) {
                                LocalDate startDate = LocalDate.parse(dis.readUTF());
                                LocalDate endDate = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = dis.readUTF();
                                if (description.equals("")) {
                                    description =  null;
                                }
                                Organization.Position position = new Organization.Position(
                                        startDate,
                                        endDate,
                                        title,
                                        description);
                                positions.add(position);
                            }
                            organizations.add(new Organization(homePage, positions));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizations));
                        break;
                }
            }
            return resume;
        }
    }
}