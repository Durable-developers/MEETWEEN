import { ComponentPropsWithoutRef, ElementType } from 'react';

import { IBoxStyle, getBoxStyle } from './Box.style';

interface BoxProps extends ComponentPropsWithoutRef<'div'> {
  tag?: ElementType;
  styles?: IBoxStyle;
}

const Box = ({ tag = 'div', styles = {}, children }: BoxProps) => {
  const Tag = tag;
  return <Tag css={getBoxStyle(styles)}>{children}</Tag>;
};

export default Box;