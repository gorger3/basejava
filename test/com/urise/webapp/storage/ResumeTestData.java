package com.urise.webapp.storage;

import com.urise.webapp.model.*;

import java.util.Arrays;
import java.util.List;

public class ResumeTestData {

    //    private static final String CONTACT_1_TITLE = "Тел.: ";
    private static final String TEL_CT = "+7(921) 855-0482";
    private static final String SKYPE_CT = "skype:grigory.kislin";
    private static final String MAIL_CT = "gkislin@yandex.ru";
    private static final String LINKED_IN_CT = "LinkedIn";
    private static final String GIT_HUB_CT = "GitHub";
    private static final String STACKOVERFLOW_CT = "Stackoverflow";
    private static final String HOME_PAGE_CT = "https://github.com/JavaWebinar/basejava/";

    private static final String OBJECTIVE_STR =
            "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям";

    private static final String PERSONAL_STR =
            "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.";


    private static final List<String> LIST_OF_CONTACTS = Arrays
            .asList(TEL_CT, SKYPE_CT, MAIL_CT, LINKED_IN_CT, GIT_HUB_CT, STACKOVERFLOW_CT, HOME_PAGE_CT);


    private static final String ACH_1 = "Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет";
    private static final String ACH_2 = "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.";
    private static final String ACH_3 = "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.";
    private static final String ACH_4 = "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.";
    private static final String ACH_5 = "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.";
    private static final String ACH_6 = "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).";
    private static final String ACH_7 = "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.";

    // создание списка достижений
    private static List<String> ACHIEVEMENT_LIST = Arrays.asList(
            ACH_1, ACH_2, ACH_3, ACH_4, ACH_5, ACH_6, ACH_7);

    private static final String QLF_01 = "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2";
    private static final String QLF_02 = "Version control: Subversion, Git, Mercury, ClearCase, Perforce";
    private static final String QLF_03 = "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB";
    private static final String QLF_04 = "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy";
    private static final String QLF_05 = "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts";
    private static final String QLF_06 = "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).";
    private static final String QLF_07 = "Python: Django.";
    private static final String QLF_08 = "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js";
    private static final String QLF_09 = "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka";
    private static final String QLF_10 = "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.";
    private static final String QLF_11 = "Инструменты: Maven + plugin development, Gradle, настройка Ngnix";
    private static final String QLF_12 = "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer";
    private static final String QLF_13 = "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования";
    private static final String QLF_14 = "Родной русский, английский \"upper intermediate\"";

    // создание списка квалификаций
    private static List<String> QUALIFICATION_LIST = Arrays.asList(
            QLF_01, QLF_02, QLF_03, QLF_04, QLF_05, QLF_06, QLF_07, QLF_08, QLF_09, QLF_10, QLF_11, QLF_12, QLF_13, QLF_14);

    private static final String ST_01 = "10/2013";
    private static final String ST_02 = "10/2014";
    private static final String ST_03 = "04/2012";
    private static final String ST_04 = "12/2010";
    private static final String ST_05 = "06/2008";
    private static final String ST_06 = "03/2007";
    private static final String ST_07 = "01/2005";
    private static final String ST_08 = "09/1997";
    private static final String ST_09 = "03/2013";
    private static final String ST_10 = "03/2011";
    private static final String ST_11 = "01/2005";
    private static final String ST_12 = "09/1997";
    private static final String ST_13 = "09/1993";
    private static final String ST_14 = "09/1987";
    private static final String ST_15 = "09/1984";

    private static final String END_01 = "Сейчас";
    private static final String END_02 = "01/2016";
    private static final String END_03 = "10/2014";
    private static final String END_04 = "04/2012";
    private static final String END_05 = "12/2010";
    private static final String END_06 = "06/2008";
    private static final String END_07 = "02/2007";
    private static final String END_08 = "01/2005";
    private static final String END_09 = "05/2013";
    private static final String END_10 = "04/2011";
    private static final String END_11 = "04/2005";
    private static final String END_12 = "03/1998";
    private static final String END_13 = "07/1996";
    private static final String END_14 = "07/1993";
    private static final String END_15 = "06/1987";

    private static final String TITLE_01 = "Автор проекта.";
    private static final String TITLE_02 = "Старший разработчик (backend)";
    private static final String TITLE_03 = "Java архитектор";
    private static final String TITLE_04 = "Ведущий программист";
    private static final String TITLE_05 = "Ведущий специалист";
    private static final String TITLE_06 = "Разработчик ПО";
    private static final String TITLE_07 = "Разработчик ПО";
    private static final String TITLE_08 = "Инженер по аппаратному и программному тестированию";
    private static final String TITLE_09 = "";
    private static final String TITLE_10 = "";
    private static final String TITLE_11 = ""; // Siemens AG
    private static final String TITLE_12 = ""; // Alcatel
    private static final String TITLE_13 = "Аспирантура (программист С, С++)"; // ИТМО
    private static final String TITLE_14 = "Инженер (программист Fortran, C)"; // ИТМО
    private static final String TITLE_15 = "";

    private static final String DSC_01 = "Создание, организация и проведение Java онлайн проектов и стажировок.";
    private static final String DSC_02 = "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.";
    private static final String DSC_03 = "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python";
    private static final String DSC_04 = "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.";
    private static final String DSC_05 = "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)";
    private static final String DSC_06 = "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining).";
    private static final String DSC_07 = "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).";
    private static final String DSC_08 = "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).";
    private static final String DSC_09 = "'Functional Programming Principles in Scala' by Martin Odersky";
    private static final String DSC_10 = "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'";
    private static final String DSC_11 = "3 месяца обучения мобильным IN сетям (Берлин)";
    private static final String DSC_12 = "6 месяцев обучения цифровым телефонным сетям (Москва)";
    private static final String DSC_13 = "";
    private static final String DSC_14 = "";
    private static final String DSC_15 = "Закончил с отличием";

    // задание периодов
    private static final Period PRD_01 = new Period(ST_01, END_01, TITLE_01, DSC_01);
    private static final Period PRD_02 = new Period(ST_02, END_02, TITLE_02, DSC_02);
    private static final Period PRD_03 = new Period(ST_03, END_03, TITLE_03, DSC_03);
    private static final Period PRD_04 = new Period(ST_04, END_04, TITLE_04, DSC_04);
    private static final Period PRD_05 = new Period(ST_05, END_05, TITLE_05, DSC_05);
    private static final Period PRD_06 = new Period(ST_06, END_06, TITLE_06, DSC_06);
    private static final Period PRD_07 = new Period(ST_07, END_07, TITLE_07, DSC_07);
    private static final Period PRD_08 = new Period(ST_08, END_08, TITLE_08, DSC_08);
    private static final Period PRD_09 = new Period(ST_09, END_09, TITLE_09, DSC_09);
    private static final Period PRD_10 = new Period(ST_10, END_10, TITLE_10, DSC_10);
    private static final Period PRD_11 = new Period(ST_11, END_11, TITLE_11, DSC_11);
    private static final Period PRD_12 = new Period(ST_12, END_12, TITLE_12, DSC_12);
    private static final Period PRD_13 = new Period(ST_13, END_13, TITLE_13, DSC_13);
    private static final Period PRD_14 = new Period(ST_14, END_14, TITLE_14, DSC_14);
    private static final Period PRD_15 = new Period(ST_15, END_15, TITLE_15, DSC_15);

    // объединение периодов в списки. В каждом списке периоды, относящиеся к одной и той же организации
    private static final List<Period> PERIODS_IN_ORG_01 = Arrays.asList(PRD_01);
    private static final List<Period> PERIODS_IN_ORG_02 = Arrays.asList(PRD_02);
    private static final List<Period> PERIODS_IN_ORG_03 = Arrays.asList(PRD_03);
    private static final List<Period> PERIODS_IN_ORG_04 = Arrays.asList(PRD_04);
    private static final List<Period> PERIODS_IN_ORG_05 = Arrays.asList(PRD_05);
    private static final List<Period> PERIODS_IN_ORG_06 = Arrays.asList(PRD_06);
    private static final List<Period> PERIODS_IN_ORG_07 = Arrays.asList(PRD_07, PRD_11);
    private static final List<Period> PERIODS_IN_ORG_08 = Arrays.asList(PRD_08, PRD_12);
    private static final List<Period> PERIODS_IN_ORG_09 = Arrays.asList(PRD_09);
    private static final List<Period> PERIODS_IN_ORG_10 = Arrays.asList(PRD_10);
    private static final List<Period> PERIODS_IN_ORG_11 = Arrays.asList(PRD_13, PRD_14);
    private static final List<Period> PERIODS_IN_ORG_12 = Arrays.asList(PRD_15);

    // задание имён и сайтов организаций
    private static final String NAME_01 = "Java Online Projects";
    private static final String SITE_01 = "http://javaops.ru/";
    private static final String NAME_02 = "Wrike";
    private static final String SITE_02 = "https://www.wrike.com/";
    private static final String NAME_03 = "RIT Center";
    private static final String SITE_03 = "";
    private static final String NAME_04 = "Luxoft (Deutsche Bank)";
    private static final String SITE_04 = "http://www.luxoft.ru/";
    private static final String NAME_05 = "Yota";
    private static final String SITE_05 = "https://www.yota.ru/";
    private static final String NAME_06 = "Enkata";
    private static final String SITE_06 = "http://enkata.com/";
    private static final String NAME_07 = "Siemens AG";
    private static final String SITE_07 = "https://www.siemens.com/ru/ru/home.html";
    private static final String NAME_08 = "Alcatel";
    private static final String SITE_08 = "http://www.alcatel.ru/";
    private static final String NAME_09 = "Coursera";
    private static final String SITE_09 = "https://www.coursera.org/course/progfun";
    private static final String NAME_10 = "Luxoft";
    private static final String SITE_10 = "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366";
    private static final String NAME_11 = "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики";
    private static final String SITE_11 = "http://www.ifmo.ru/";
    private static final String NAME_12 = "Заочная физико-техническая школа при МФТИ";
    private static final String SITE_12 = "https://mipt.ru/";

    // создание рабочих организаций
    private static final Organization ORG_01 = new Organization(NAME_01, SITE_01, PERIODS_IN_ORG_01, 0);
    private static final Organization ORG_02 = new Organization(NAME_02, SITE_02, PERIODS_IN_ORG_02, 0);
    private static final Organization ORG_03 = new Organization(NAME_03, SITE_03, PERIODS_IN_ORG_03, 0);
    private static final Organization ORG_04 = new Organization(NAME_04, SITE_04, PERIODS_IN_ORG_04, 0);
    private static final Organization ORG_05 = new Organization(NAME_05, SITE_05, PERIODS_IN_ORG_05, 0);
    private static final Organization ORG_06 = new Organization(NAME_06, SITE_06, PERIODS_IN_ORG_06, 0);
    private static final Organization ORG_07_0 = new Organization(NAME_07, SITE_07, PERIODS_IN_ORG_07, 0);
    private static final Organization ORG_08_0 = new Organization(NAME_08, SITE_08, PERIODS_IN_ORG_08, 0);

    // создание списка рабочих организаций
    private static List<Organization> EXPERIENCE_LIST = Arrays.asList(
            ORG_01, ORG_02, ORG_03, ORG_04, ORG_05, ORG_06, ORG_07_0, ORG_08_0);

    // создание учебных организаций
    private static final Organization ORG_09 = new Organization(NAME_09, SITE_09, PERIODS_IN_ORG_09, 0);
    private static final Organization ORG_10 = new Organization(NAME_10, SITE_10, PERIODS_IN_ORG_10, 0);
    private static final Organization ORG_07_1 = new Organization(NAME_07, SITE_07, PERIODS_IN_ORG_07, 1);
    private static final Organization ORG_08_1 = new Organization(NAME_08, SITE_08, PERIODS_IN_ORG_08, 1);
    private static final Organization ORG_11_0 = new Organization(NAME_11, SITE_11, PERIODS_IN_ORG_11, 0);
    private static final Organization ORG_11_1 = new Organization(NAME_11, SITE_11, PERIODS_IN_ORG_11, 1);
    private static final Organization ORG_12 = new Organization(NAME_12, SITE_12, PERIODS_IN_ORG_12, 0);

    // создание списка учебных организаций
    private static List<Organization> EDUCATION_LIST = Arrays.asList(
            ORG_09, ORG_10, ORG_07_1, ORG_08_1, ORG_11_0, ORG_11_1, ORG_12);


    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");
        System.out.println(resume.getFullName());
        // задание контактов
        int i = 0;
        for (ContactType type : ContactType.values()) {
            resume.setContact(type, LIST_OF_CONTACTS.get(i));
            i++;
        }
        // вывод контактов
        for (ContactType type : ContactType.values()) {
            String str = resume.getContact(type);
            System.out.println(type.getType() + str);
        }

        //задание текстовых секций
        AbstractSection objectiveSection = new TextSection(OBJECTIVE_STR);
        AbstractSection personalSection = new TextSection(PERSONAL_STR);
        resume.setSection(SectionType.OBJECTIVE, objectiveSection);
        resume.setSection(SectionType.PERSONAL, personalSection);

        // задание строковой секции Достижения
        AbstractSection achievementSection = new ListSection(ACHIEVEMENT_LIST);
        resume.setSection(SectionType.ACHIEVEMENT, achievementSection);

        // задание строковой секции Квалификации
        AbstractSection qualificationSection = new ListSection(QUALIFICATION_LIST);
        resume.setSection(SectionType.QUALIFICATIONS, qualificationSection);

        // задание секции рабочих организаций
        AbstractSection experienceSection = new OrganizationSection(EXPERIENCE_LIST);
        resume.setSection(SectionType.EXPERIENCE, experienceSection);

        // задание секции учебных организаций
        AbstractSection educationSection = new OrganizationSection(EDUCATION_LIST);
        resume.setSection(SectionType.EDUCATION, educationSection);

        // вывод на консоль всех секций
        for (SectionType type : SectionType.values()) {
            Object content = resume.getSection(type).toString();
            System.out.println(type.getType());
            System.out.println(content);
        }
    }


}
