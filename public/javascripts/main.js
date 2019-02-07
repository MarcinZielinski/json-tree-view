const tree = document.querySelectorAll('ul.tree a');
tree.forEach(treeNode =>
    treeNode.addEventListener('click', event => {
            const parent = event.target.parentElement;
            const classList = parent.classList;
            if (classList.contains('open')) {
                classList.remove('open');
                const openedChildren = parent.querySelectorAll(':scope .open');
                openedChildren.forEach(sub => sub.classList.remove('open'))
            } else {
                classList.add('open');
            }
            event.preventDefault();
        }
    )
);