# 🌿 ZaytounaRH – Système de Gestion des Ressources Humaines (JavaFX)

📝 Overview
**ZaytounaRH** est une application desktop développée avec **Java 17** et **JavaFX** dans le cadre du module **PIDEV 3A** à *Esprit School of Engineering*. Elle a pour but de moderniser la gestion des ressources humaines au sein des entreprises, en proposant des modules essentiels tels que le recrutement, la formation, la gestion des présences, les réclamations, la finance, etc., dans une interface conviviale et modulaire.

 
## 📌 Table des Matières

* [🎯 Overview](#-présentation)
* [✨ Fonctionnalités](#-fonctionnalités)
* [🧰 Stack Technique](#-stack-technique)
* [🗂️ Structure du Projet](#️-structure-du-projet)
* [🚀 Mise en Route](#-mise-en-route)
* [🤝 Remerciements](#-remerciements)

 

## ✨ Fonctionnalités

* 🔍 **Gestion des recrutements**
* 👤 **Gestion des utilisateurs**
* 🎓 **Gestion des formations**
* 🕒 **Suivi de présence**
* 💬 **Réclamations et incidents**
* 💵 **Gestion financière**

 
## 🧰 Stack Technique

**Langage & Outils**

* Java 17 (JDK)
* JavaFX (UI)
* Scene Builder (pour le design des interfaces FXML)
* Maven (gestion de projet)
* JDBC (connexion base de données)
* MySQL ou MariaDB (base de données relationnelle)

**Tests et outils complémentaires**

  * IntelliJ IDEA 


## 🗂️ Structure du Projet

ZaytounaRH/
├── src/
│   └── main/
│       └── java/
│           └── tn/esprit/
│               ├── controllers/      # Contrôleurs JavaFX
│               ├── models/           # Entités (modèles de données)
│               ├── services/         # Logique métier
│               ├── utils/            # Utilitaires (DB, sessions, etc.)
│               └── tests/            # Classe Main de test
├── resources/
│   ├── views/                        # Fichiers FXML (interfaces)
│   ├── styles/                       # Fichiers CSS
│   └── images/                       # Ressources graphiques
├── .env                              # Configuration DB (optionnel)
├── pom.xml                           # Dépendances Maven
└── README.md
```

---

## 🚀 Mise en Route

1. **Cloner le dépôt**

```bash
git clone https://github.com/ZaytounaRH/ZaytounaRH.git
cd zaytouna-rh
```

2. **Ouvrir le projet avec IntelliJ IDEA ou Eclipse**

3. **Configurer la base de données**

   * Créer une base de données `zaytouna_rh` dans MySQL
   * Mettre à jour les paramètres de connexion dans un fichier `DBConfig.java` ou `.env`

4. **Exécuter les scripts SQL de création de tables** (fournis dans `/resources/sql/` si disponibles)

5. **Lancer l'application via la classe `Main.java`**

 

## 🤝 Remerciements

Ce projet a été réalisé sous la supervision de **M. Amir Yazidi**, dans le cadre d’un travail pédagogique collaboratif à *Esprit School of Engineering*.

Un grand merci à :

* Toute l’équipe de développement de **ZaytounaRH**
* Nos enseignants et encadrants pour leur accompagnement

 
 
