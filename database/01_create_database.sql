-- =============================================
-- FarmConnect - Database Schema
-- File 1: Create Database and Tables
-- Run this FIRST
-- =============================================

USE master;
GO

-- Create the database if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'FarmConnect')
BEGIN
    CREATE DATABASE FarmConnect;
END
GO

USE FarmConnect;
GO

-- =============================================
-- DROP existing tables (in order due to FK)
-- =============================================
IF OBJECT_ID('dbo.OrderItem', 'U') IS NOT NULL DROP TABLE dbo.OrderItem;
IF OBJECT_ID('dbo.[Order]', 'U') IS NOT NULL DROP TABLE dbo.[Order];
IF OBJECT_ID('dbo.Product', 'U') IS NOT NULL DROP TABLE dbo.Product;
IF OBJECT_ID('dbo.Customer', 'U') IS NOT NULL DROP TABLE dbo.Customer;
IF OBJECT_ID('dbo.[User]', 'U') IS NOT NULL DROP TABLE dbo.[User];
GO

-- =============================================
-- CREATE TABLES
-- =============================================

-- 1. User table
CREATE TABLE dbo.[User] (
    [ID] INT IDENTITY(1,1) PRIMARY KEY,
    [Email] NVARCHAR(255) NOT NULL UNIQUE,
    [Name] NVARCHAR(255) NOT NULL,
    [Address] NVARCHAR(255),
    [Password] NVARCHAR(255) NOT NULL
);
GO

-- 2. Customer table
CREATE TABLE dbo.Customer (
    [CustomerId] INT IDENTITY(1,1) PRIMARY KEY,
    [CustomerName] NVARCHAR(100) NOT NULL,
    [CustomerSdt] NVARCHAR(20),
    [CustomerEmail] NVARCHAR(100),
    [CustomerAddress] NVARCHAR(200),
    [UserID] INT NOT NULL,
    [CreatedDate] DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Customer_User FOREIGN KEY ([UserID]) REFERENCES dbo.[User]([ID])
);
GO

-- 3. Product table
CREATE TABLE dbo.Product (
    [ProId] INT IDENTITY(1,1) PRIMARY KEY,
    [ProName] NVARCHAR(255) NOT NULL,
    [Des] NVARCHAR(MAX),
    [Quantity] INT NOT NULL DEFAULT 0,
    [Price] DECIMAL(18, 2) NOT NULL DEFAULT 0,
    [Unit] NVARCHAR(50),
    [Type] NVARCHAR(50),
    [ProductionDate] DATETIME,
    [ExpirationDate] DATETIME,
    [PricePercent] FLOAT DEFAULT 0,
    [UserID] INT,
    CONSTRAINT FK_Product_User FOREIGN KEY ([UserID]) REFERENCES dbo.[User]([ID])
);
GO

-- 4. Order table
CREATE TABLE dbo.[Order] (
    [OrderId] INT IDENTITY(1,1) PRIMARY KEY,
    [UserID] INT NOT NULL,
    [CustomerId] INT NULL,
    [OrderTime] DATETIME DEFAULT GETDATE(),
    [CustomerName] NVARCHAR(100),
    [CustomerSdt] NVARCHAR(20),
    [TotalAmount] DECIMAL(18, 2),
    CONSTRAINT FK_Order_User FOREIGN KEY ([UserID]) REFERENCES dbo.[User]([ID]),
    CONSTRAINT FK_Order_Customer FOREIGN KEY ([CustomerId]) REFERENCES dbo.Customer([CustomerId])
);
GO

-- 5. OrderItem table
CREATE TABLE dbo.OrderItem (
    [ItemId] INT IDENTITY(1,1) PRIMARY KEY,
    [OrderId] INT NOT NULL,
    [ProId] INT NOT NULL,
    [Quantity] INT NOT NULL DEFAULT 1,
    [UnitPrice] DECIMAL(18, 2) NOT NULL,
    CONSTRAINT FK_OrderItem_Order FOREIGN KEY ([OrderId]) REFERENCES dbo.[Order]([OrderId]),
    CONSTRAINT FK_OrderItem_Product FOREIGN KEY ([ProId]) REFERENCES dbo.Product([ProId])
);
GO

-- =============================================
-- CREATE INDEXES
-- =============================================
CREATE INDEX IX_Product_UserID ON dbo.Product([UserID]);
CREATE INDEX IX_Customer_UserID ON dbo.Customer([UserID]);
CREATE INDEX IX_Order_UserID ON dbo.[Order]([UserID]);
CREATE INDEX IX_Order_CustomerId ON dbo.[Order]([CustomerId]);
CREATE INDEX IX_OrderItem_OrderId ON dbo.OrderItem([OrderId]);
CREATE INDEX IX_OrderItem_ProId ON dbo.OrderItem([ProId]);
GO

PRINT '';
PRINT '=== DATABASE CREATED SUCCESSFULLY ===';
PRINT 'Database: FarmConnect';
PRINT 'Tables: User, Customer, Product, Order, OrderItem';
PRINT '';
PRINT 'Next: Run 02_insert_data.sql to add sample data';
GO
