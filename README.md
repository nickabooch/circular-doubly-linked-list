# Circularly-Linked

## Project Overview

This proj goes back and revisits the implementation of doubly-linked lists by introducing a circularly-linked structure with a dummy node. The main goal is to really simplify handling edge cases associated with list operations, particularly at the boundaries of the list. Additionally, this implementation includes what SamR calls a "fail fast" strategy for iterators, which quickly detects and responds to concurrent modifications, thus enhancing error handling and robustness.

### Features

- **Circularly-Linked List**: Enhances the standard doubly-linked list by connecting the end of the list back to the beginning through a dummy node, facilitating seamless iteration and modification at both ends of the list.
- **Dummy Node**: A non-removable node that simplifies operations by eliminating the need for null checks and special cases at the boundaries.
- **Fail Fast Iterators**: Iterators that throw a `ConcurrentModificationException` if the list is structurally modified after the iterator is created, except through the iterator’s own methods.

## Installation and Setup

Clone the repository from GitHub:

```bash
git clone https://github.com/nickabooch/circular-doubly-linked-list.git
cd circular-doubly-linked-list
